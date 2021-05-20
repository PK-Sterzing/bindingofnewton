<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.5" tiledversion="1.5.0" name="room" tilewidth="48" tileheight="48" tilecount="21" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <image width="32" height="32" source="../img/wall.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="32" height="32"/>
  </objectgroup>
 </tile>
 <tile id="1">
  <image width="32" height="32" source="../img/wall-corner.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="32" height="32"/>
   <object id="2" x="0" y="0" width="32" height="32"/>
  </objectgroup>
 </tile>
 <tile id="2">
  <image width="32" height="32" source="../img/floor.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="0" width="32" height="32"/>
  </objectgroup>
 </tile>
 <tile id="3">
  <image width="33" height="32" source="../img/stein1.png"/>
  <objectgroup draworder="index" id="3">
   <object id="2" x="15.3293" y="6.31999">
    <polygon points="0,0 4.03404,-3.22723 15.4638,2.01702 15.8672,19.9013 13.1779,24.8766 4.97531,24.3387 -4.97531,25.1455 -13.3123,21.1115 -14.1191,9.41275 -13.1779,0.806807 -6.18552,-0.941275"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="5">
  <image width="32" height="32" source="../img/stein2.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="18.2876" y="3.22723">
    <polygon points="0,0 8.47148,2.55489 11.9676,6.85786 12.1021,17.4808 12.7745,22.0527 8.06807,26.8936 1.21021,27.297 -2.82383,25.5489 -7.93361,25.6834 -12.5055,27.028 -16.4051,23.3974 -17.8842,17.4808 -17.7498,9.00935 -14.5225,5.10978 -6.7234,1.61361"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="6">
  <image width="32" height="32" source="../img/stein3.png"/>
  <objectgroup draworder="index" id="2">
   <object id="1" x="6.85786" y="13.1779">
    <polygon points="0,0 8.47148,-8.20254 15.0604,-10.0851 21.7838,-6.7234 23.6664,2.82383 23.8008,14.1191 17.3464,17.6153 10.0851,16.4051 2.82383,16.674 -3.3617,15.5983 -4.57191,11.4298 -2.68936,7.93361 -3.7651,3.22723"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="11">
  <image width="32" height="48" source="../img/fire1.png"/>
  <animation>
   <frame tileid="12" duration="100"/>
   <frame tileid="11" duration="100"/>
   <frame tileid="13" duration="100"/>
  </animation>
 </tile>
 <tile id="12">
  <image width="32" height="48" source="../img/fire2.png"/>
 </tile>
 <tile id="13">
  <image width="32" height="48" source="../img/fire3.png"/>
 </tile>
 <tile id="14">
  <image width="48" height="48" source="../../instruction_shoot.png"/>
 </tile>
 <tile id="15">
  <image width="48" height="48" source="../../instruction_walk.png"/>
 </tile>
 <tile id="16">
  <image width="32" height="32" source="../img/door.png"/>
 </tile>
 <tile id="17">
  <image width="32" height="32" source="../img/door-open.png"/>
 </tile>
 <tile id="18">
  <image width="32" height="32" source="../img/floor-stone.png"/>
 </tile>
 <tile id="19">
  <image width="32" height="32" source="../img/floor-stone-hole.png"/>
 </tile>
 <tile id="20">
  <image width="32" height="32" source="../img/floor-stone-spikes.png"/>
 </tile>
 <tile id="21">
  <image width="32" height="32" source="../img/wall-stone.png"/>
 </tile>
 <tile id="22">
  <image width="32" height="32" source="../img/wall-stone-corner.png"/>
 </tile>
 <tile id="23">
  <image width="32" height="32" source="../img/floor-stone-hole-full.png"/>
 </tile>
 <tile id="24">
  <image width="32" height="32" source="../img/floor-stone-hole-corner.png"/>
 </tile>
 <tile id="25">
  <image width="32" height="32" source="../img/floor-stone-hole-edge.png"/>
 </tile>
</tileset>
