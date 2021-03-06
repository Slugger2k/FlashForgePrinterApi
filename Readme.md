
# FlashForge Adventurer 3 Printer API 
This library enables you to communicate with your FlashForge Adventurer 3 printer and maybe other 
models from FlashForge. It is completely written in Java and communicates with the printer 
via raw TCP. 
You can use this library to build your own monitoring or printing tools.

The library find's your printer in the network with a UDP broadcast message which is send when you 
instantiate the UpdDiscoveryClient class.

    Broadcast: c0a800de46500000 => 16Bytes to 225.0.0.9:19000
    Replay from 192.168.0.247: My 3D Printer




## The following commands are implemented so far:

- HELLO = M601 S1

      CMD M601 Received.
      Control Success.
      ok

- BYE = M602

      CMD M602 Received.
      Control Release.
      ok

- INFO = 115
  
      CMD M115 Received.
      Machine Type: FlashForge Adventurer III
      Machine Name: My 3D Printer
      Firmware: v1.1.7
      SN: XXXXXXXXXXXX
      X: 150 Y: 150 Z: 150
      Tool Count: 1
      Mac Address: 88:A9:A7:XX:XX:XX
      ok

- INFO_STATUS = M119

      CMD M119 Received.
      Endstop: X-max:1 Y-max:0 Z-max:0
      MachineStatus: READY
      MoveMode: READY
      Status: S:1 L:0 J:0 F:0
      ok

- INFO_XYZAB = M114

      CMD M114 Received.
      X:0 Y:0 Z:0 A:0 B:0
      ok

- INFO_CAL = M650

      CMD M650 Received.
      X: 1.0 Y: 0.5
      ok

- TEMP = M105

      CMD M105 Received.
      T0:22 /0 B:11/0
      ok

- LED_ON = M146 r255 g255 b255 F0

      CMD M146 Received.
      ok

- LED_OFF = M146 r0 g0 b0 F0

      CMD M146 Received.
      ok

- PRINT_STATUS = M27

      CMD M27 Received.
      SD printing byte 0/100
      ok

- SAVE_FILE = M29

      CMD M29 Received.
      Done saving file.
      ok

- PREPARE_PRINT = M28 size filename

      CMD M28 Received.
      Writing to file: 0:/user/Snake.gx
      ok
      Send data: 4112 bytes
      N0000 ok.
      Send data: 4112 bytes
      N0001 ok.
      Send data: 4112 bytes
      N0002 ok.
      Send data: 4112 bytes
      N0003 ok.
      .
      .
      .

- PRINT_START = M23 filename
  
      CMD M23 Received.
      File opened: 20mm_Box.gx Size: 154674
      File selected
      ok

- PRINT_STOP = M26

      CMD M26 Received.
      ok



## Example:

    AdventurerClient client = new AdventurerClient(new FlashForgeDiscoverClient().getPrinterAddress());
    client.print(Paths.get("/home/slg/eclipse-workspace-2020/printertool/20mm_Box.gx"));



### Sequenz to transfer a file to the printer and start the print.
```mermaid
graph LR
A[PREPARE_PRINT] --> B[SAVE_FILE]
B[SAVE_FILE] --> C[PRINT]
```

## Transfer data packet header

For transferring data to the printer you have to split the packets in 4096 byte pieces and build the CRC32 checksum from it. 
You also have to count the real length of the packet because of the last packet need to know its size, a packet counter is also needed.


#### Header:

|          |  header  |  counter |  length  |  CRC32   |
|----------|----------|----------|----------|----------|
| packet   | 5a5aa5a5 | 0000026d | 00000b7e | 66d6707b |
| ASCII    |     ZZ¥¥ |      621 |     2942 |          |



## FAQ

- There is currently only logging to sysout implemented, but it's easy to replace because 
  there are only System.out.println() calls for info logging and in Exceptions e.printStackTrace() calls 
  for error logging.

Working in progress...

if you have any questions please drop me a mail `slg at slg.ddnss.de`

