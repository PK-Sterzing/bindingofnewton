<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.5" tiledversion="1.6.0" name="room" tilewidth="32" tileheight="48" tilecount="10" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <image width="32" height="32" source="../../img/lava-corner.png"/>
 </tile>
 <tile id="1">
  <image width="32" height="32" source="../../img/lava-edge.png"/>
 </tile>
 <tile id="2">
  <image width="32" height="32" source="../../img/lava-floor.png"/>
 </tile>
 <tile id="5">
  <image width="32" height="48" source="../../img/fire1.png"/>
 </tile>
 <tile id="6">
  <image width="32" height="48" source="../../img/fire2.png"/>
 </tile>
 <tile id="7">
  <image width="32" height="48" source="../../img/fire3.png"/>
  <animation>
   <frame tileid="5" duration="100"/>
   <frame tileid="6" duration="100"/>
   <frame tileid="7" duration="100"/>
  </animation>
 </tile>
 <tile id="8">
  <image width="32" height="32" source="../../img/3floor-bossdoor-open.png"/>
 </tile>
 <tile id="9">
  <image width="32" height="32" source="../../img/3rd-door.png"/>
 </tile>
 <tile id="10">
  <image width="32" height="32" source="../../img/3rd-door-open.png"/>
 </tile>
 <tile id="11">
  <image width="32" height="32" source="../../img/3floor-bossdoor-closed.png"/>
 </tile>
</tileset>
