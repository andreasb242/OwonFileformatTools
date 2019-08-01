# OwonFileformatTools
Owon XDS3000 Series Fileformat / Tools hacks and Tipps

## Owon .bin file Format
The file ends with `.bin`
the contents of the file is: `SPBXD`
Followed by 4bytes, which is the length of the JSON
Followed by the JSON, e.g.
```json
{"MODEL":"306200103","IDN":"OWON,XDS3062A,1707604,V5.8.0","channel":[{"Index":"CH1","Availability_Flag":"TRUE","Display_Switch":"OFF","Wave_Character":"CH1","Sample_Rate":"(200MS/s)","Acqu_Mode":"SAMPle","Storage_Depth":"40M","Display_Mode":"Normal","Hscale":"10ms","Vscale":"20.0mV","Reference_Zero":"0","Scroll_Pos_Time":"0.000000us","Trig_After_Time":"3.430000us","Trig_Tops_Tme":"0.000000us","Adc_Data_Time":"0.005000us","Adc_Data0_Time":"0.000000us","Voltage_Rate":"1.250000mv","Data_Length":"40000000","Probe_Magnification":"1X","Current_Rate":10000.000000,"Current_Ratio":12.500000,"Measure_Current_Switch":"OFF","Cyc":"0.000000us","Freq":"0.000000Hz","PRECISION":0},]
```

Followed by the Binary data.


## Get the Owon Tools working on Linux
Download the Owon Tools, unzip it. Also extract the data out of the .exe.

Check in the Plugin folder, there is a org.eclipse.swt*.jar
This works only with Windows, the Linux Version is neede.

For Version 1.4 download a Version of Eclipse Neon, and copy:
 `org.eclipse.swt.gtk.linux.x86_64_3.105.1.v20160907-0248.jar`

to the Plugin folder.

Then open the `config.ini` file in configuration folder.
The last line is the SWT Windows implementation, replace it with:
  `org.eclipse.swt.gtk.linux.x86_64`

So the config looks now like:
```
#Product Runtime Configuration File
eclipse.application=com.owon.uppersoft.hdoscilloscope.application
osgi.bundles.defaultStartLevel=4
eclipse.product=com.owon.uppersoft.hdoscilloscope.product
osgi.splashPath=platform:/base/plugins/com.owon.uppersoft.hdoscilloscope
osgi.bundles=JSON,\
  RXTXcomm,\
  ch.ntb.usb,\
  com.owon.uppersoft.common,\
  com.owon.uppersoft.hdoscilloscope,\
  jxl,\
  org.eclipse.core.commands,\
  org.eclipse.core.contenttype,\
  org.eclipse.core.jobs,\
  org.eclipse.core.runtime@start,\
  org.eclipse.equinox.app,\
  org.eclipse.equinox.common@2:start,\
  org.eclipse.equinox.preferences,\
  org.eclipse.equinox.registry,\
  org.eclipse.jface,\
  org.eclipse.swt,\
  org.eclipse.swt.gtk.linux.x86_64
```

Download
http://rxtx.qbang.org/pub/rxtx/rxtx-2.1-7-bins-r2.zip

copy
 `rxtx.qbang.org/pub/rxtx/rxtx-2.1-7-bins-r2.zip`

to
 `plugins/RXTXcomm_2.1.7`


There is still an issue with libusb, there is only a 32bit Version there.
However, you can now start the tooling with:
 `java -jar ./plugins/org.eclipse.equinox.launcher_*.jar`

You cannot connec by network or by USB because of this, but you can open Owon files






