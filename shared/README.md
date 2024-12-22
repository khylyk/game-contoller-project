# The Frog v3.0

The Frog, a space explorer vehicle from the Saxion Space Agency (SASA), is about to wrap up a mission and was heading back to the base on the recently discovered asteroid S42, hoping to find precious minerals, as disaster strikes; an avalanche caused rocks to block the entry to the base and also knocked out the navigation software of the Frog (called the Pilot). 

In order to retrieve the very expensive Frog, it needs to get back to the base where it will be beamed up with a tractor beam. The only solution appears to be to blow up the obstruction to clear the entry. And luckaly there is still TNT and a Detonator present on the asteroid, but because of the avalanche, we don't know where it is. The Frog has to find and collect it and drop it near the obstruction.  

Communication between the base, the Frog and Mission Control, back on earth, is via a satellite orbiting the asteroid, that distributes to all messages. Depending on the transmission distance it may take a longer time for the message to arrive. Therefore transmissions to and from Mission Control, which is back on th earth, have a serious lag. All the other participants have a fast connection, as they are on the asteroid's surface and near the satellite. 

Mission control has a detailed map of the asteroids surface and can guide the Frog, although not navigate it directly, because of the formentioned huge lag in the transmission. 

The astronaut driver of the Frog suffered some injuries during the avalanche and is not able to drive the Frog for extended times. Therefore it is the task of the software team to write software, called The Pilot, for the navigation computer, so that the Frog can drive mostly autonomous to given locations on the astroid. 
The Frog can provide information on the actual location, heading and energy levels through messages send to the satelite (and the Pilot). This communication interface can also be used to drive the Frog and use the Frog's radar to scan the area for obstructions.

When the Frog is near the TNT or the Detonator, these are automatically picked up, which is reported back by the software interface.
When the Frog is near to the obstruction at the base and if it carries both the TNT and the detonator, it will automatically drop both items and the countdown for the explosion begins. The rocks will then be blown away, sky high.

The Frog uses batteries while driving and radar scans also use energy. The batteries are charged by solar panels, but only when it is in direct sunlight.

The Radar provides blibs; obstacle locations relative to the Frog's position and heading. It does a -90° to +90° sweep at the height of the Frog's bottom and at the height of it's roof.

The Communication protocol for the Frog's part is described below. It may be extended for other communication like between Mission Control and the Pilot.
<div style="page-break-after: always;"></div>

## Map

The satelite made an image of the asteroid's surface, covering an area of 1000 x 1000 m, of which location (0, 0) is the center, where the base is.

![asteroid Map](resources/map.png)


## Coordinate system
  
The coordinate system used has x from left to right, y from down to up (i.e. height!) and z from back to front.
<div style="page-break-after: always;"></div>

## Protocol Proposal v3.01

**(proposal 2023-04-16)**

There are three, must have, roles in the game and two optional:
1. The Frog: the vehicle on the asteroid.
2. The Pilot: located at the asteroid base, navigates/drives the Frog.
3. Mission Control: globally guides the Pilot. (lagged)
5. Terminal (optional): receives, and logs all communication for analysis. It can also send messages. (lagged) 
4. Guest (optional): for only viewing the Frogs adventures on earth, receives all messages but does not send any. (lagged)

Note that FROG and PILOT are mandatory role names in this communication system. They have a fast, almost instant, communication speed. The rest of the role names, which are free to choose, experience communication with a serious lag.
<div style="page-break-after: always;"></div>

### Architecture

```plantuml
    title The Frog System Artchitecture
    cloud Orbit {
        node Satellite {
            component SaSaServer as Server
        }
    }
    cloud asteroid {
        node "The Frog" {
            component SaSaCommunicator as FC
            [Frog] -d- FC
        }
        node "The Pilot" {
            component SaSaCommunicator as PC
            database Database as PD
            [Pilot] -d- PC
            [Pilot] -r- PD
        }
    }
    
    cloud Earth{
        node "Mission Control" {
            component SaSaCommunicator as MC
            component MissionControl as M
            database Database as MD
            M -u- MC
            M -r- MD           
        }
    }
    FC -d- Server : fast
    PC -d- Server : fast
    MC -u- Server : slow
```

The databases can be used to make map data persistent and log mission data for analysis and for replay.

<div style="page-break-after: always;"></div>

### Frog Communications
The SaSaServer is a simple message broker, broadcasting every message to all receivers.

Messages are strings, consisting of two parts; <ID>, then a space and then the <PAYLOAD>
1. ID:  
   an unique name for the sender. Like FROG, PILOT, CONTROL, TERMINAL, GUEST_1, GUEST_2 etc. Names cannot have spaces.
2. PAYLOAD:   
   the message itself with space separated values with Locale.US and angles in degrees.

The next diagram shows the connection of the three clients to the SaSaServer in the Satellite. After communication is established the client makes itself known by name by sending "<name> CONNECTED". The SaSaCommunicator takes care of this. 

```plantuml
    title SaSa Connecting Clients to Server
    SaSaServer <- Frog : open communication
    SaSaServer <- Frog : FROG CONNECTED
    SaSaServer <- Pilot : open communication
    SaSaServer <- Pilot : PILOT CONNECTED
    SaSaServer <- Control : open communication
    SaSaServer <- Control : CONTROL CONNECTED
``` 
Note: there is no acknowledgement returned

<div style="page-break-after: always;"></div>

When a connection fails, the SaSaCommunicator tries to reconnect every 2 seconds as is shown in the next diagram.

```plantuml
    title Client reconnect loop 
    loop     
        GROUP IF not connected
            SaSaCommunicator -> SaSaCommunicator : wait 2s for server to init
            SaSaCommunicator -> SaSaServer : connect
            GROUP IF connected 
                SaSaCommunicator -> SaSaServer : <ID> CONNECTED
            END     
        ELSE Connected
            SaSaServer -> SaSaCommunicator : message 
            SaSaCommunicator -> SaSaServer : message 
        END
    end  
```

<div style="page-break-after: always;"></div>

### The Frog protocol.

The Frog has a fixed protocol, which is described here. Other communicating participants can define their own protocol, as long as they provide an ID and a message payload (string). 

#### From The Frog

After being connected and named, communication can start. We assume communication between FROG and PILOT here.

* The Frog sends its status info on an interval basis (0.25s) when enabled by receiving a "STATUS ON" message, or on request.
* The Frog status has it's postion (x, y, z), an angle (in degrees) and a current energy level [0..1]  and charging level [0..1].  

```plantuml
    Frog -> Pilot : FROG STATUS <x> <y> <z> <angle> <energy> <solar>
```

* The Frog sends radar info every 2 seconds when enabled with "RADAR ON" or 
  on request with "RADAR REQUEST".

```plantuml
    Frog -> Pilot : FROG RADAR START <originX> <originY> <origonZ> <angle>
    loop for each angle in range 
        Frog -> Pilot : FROG RADAR BLIP <x> <y> <z>
    end
    Frog -> Pilot : FROG RADAR STOP
```
*  Receiving Frog's pickup event at the given location including the pickups name (TNT or DETONATOR).
```plantuml
    Frog -> Pilot : FROG PICKUP <name> <x> <y> <z> 
```

* Receiving Frog's dropping a pickup at a named location ( explosion area )
```plantuml
    Frog -> Pilot : FROG DROP <name> <x> <y> <z> 
```

* Receiving the Frog touching it's base center to beam up, mission completed !
```plantuml
    Frog -> Pilot : FROG HOME <x> <y> <z> 
```

<div style="page-break-after: always;"></div>

#### To the Frog

We assume the Pilot sending here, but any SaSaCommunicator can send these for the Frog to process, with or without lag. 

* Driving the Frog. Note that the newer drive command overrules a possibly running one, there is no queue.  
    - power is ranged from -1.0..1.0 power factor, where 0 means braking,
    - angle from -30°..30° and 
    - duration 0.0..5.0s, where 0 means continue until the next drive command is received.

```plantuml
    Pilot -> Frog : PILOT DRIVE <power> <angle> <duration>   
```

* Request the Frog's status, it's position and energy data.
```plantuml
    Pilot -> Frog : PILOT STATUS REQUEST   
    Frog -> Pilot : FROG STATUS <x> <y> <z> <energy> <charging>    
```
* Enable the Frog's auto status updates, the default is off. When enabled, the Frog send its status with an interval of 0.2s. 
```plantuml
    Pilot -> Frog : PILOT STATUS ON   
    loop on interval
        Frog -> Pilot : FROG STATUS <x> <y> <z> <angle> <energy> <charging>
    end
```
* and auto status updates can be disabled again.
```plantuml
    Pilot -> Frog : PILOT STATUS OFF   
```

* Enable auto radar updates (START, BLIPS, STOP), the default is off. When ON the Frog's radar scans every 0.5s and sends its result. 
```plantuml
    Pilot -> Frog : PILOT RADAR ON   
    loop on interval
        Frog -> Pilot : FROG RADAR START <originX> <originY> <originZ> <angle>
        loop for each angle in range 
            Frog -> Pilot : FROG RADAR POINT <x> <y> <z> <angle>
        end
        Frog -> Pilot : FROG RADAR STOP
    end
```
  * When radar off it sends it on request
```plantuml
    Pilot -> Frog : PILOT RADAR OFF   
    Pilot -> Frog : PILOT RADAR REQUEST
        Frog -> Pilot : FROG RADAR START <originX> <originY> <originZ> <angle>
        loop for each angle in range 
            Frog -> Pilot : FROG RADAR POINT <x> <y> <z> <angle>
        end
        Frog -> Pilot : FROG RADAR STOP
```




