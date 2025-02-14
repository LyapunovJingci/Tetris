# Tetris

This is a clone of the evergreen video game Tetris on Android platform developed using Java and Kotlin. 

## 🎮Instruction

|button|control|
|:---|:---|
|⬆️|fast drop to the bottom|
|⬅️|move block to the left|
|➡️|move block to the right|
|⬇️|move block fast|
|Rotate|rotate block counterclockwise|

**Score** 

|Line|Score|
|:---|:---|
|Single|100|
|Double|200|
|Triple|400|
|Tetris|800|

**Level**         
Level up by clearing 20 lines.          
Initial level could be changed in the setting screen.       

**Speed**          
A block would move down 1 unit per second in level 1. Droping speed would increasing by 0.05s for every level up.        

## 🧐Design 
This demo follows MVP architectual pattern.
<img src="Screenshot/structure.png" width="95%">

## 📝Library Usage
* [Room](https://developer.android.com/training/data-storage/room)
* [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) 
* [Coroutines](https://developer.android.com/kotlin/coroutines)
* [Lottie](https://github.com/airbnb/lottie-android)
* [Konfetti](https://github.com/DanielMartinus/Konfetti)

## 🎈Screenshots

<img src="Screenshot/start.png" width="45%">&#160;<img src="Screenshot/game.png" width="45%">
<img src="Screenshot/setting.png" width="45%">&#160;<img src="Screenshot/highscore.png" width="45%">
