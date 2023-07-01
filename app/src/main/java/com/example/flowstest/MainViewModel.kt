package com.example.flowstest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * collect - Будет выполняться для каждого емита этого потока, даже если время выполнения будет больше времени работы до следующего емита
     *  countDownFlow.collectLatest{ time->
     *                  delay(100L)
     *                  println("the current time is $time")
     *              }
 * collectLaБудет выполняться для каждого емита этого потока, если время выполнения будет не больше вреиени до седующего emita
     * countDownFlow.collectLatest{ time->
     *                 delay(100L)
     *                 println("the current time is $time")
     *            }
 *
 *
 * Можно запустить через
 *      1)      viewModelScope.launch {
 *                              countDownFlow.collect{
 *                                  println("the current time is $it")
 *                              }
 *      2) countDownFlow.onEach {
 *                        println("the current time each $it")
 *                    }.launchIn(viewModelScope)
 *
 *
 * accumulator = накопленное значение
 * value = текущеее значение
 * .reduce { accumulator, value ->
 *                    accumulator + value
 *                }
 * fold тоже что и reduce только со стартовым накопителем
 * .fold(100) { accumulator, value ->
 *                     accumulator + value
 *                 }
 *
 *  Сглаживание
 *  flow1.flatMapConcat - Объеденение потоков
 *
 *
 *  Strategy
 *  1 - buffer() выполнение в других спопрограамах
 *  2 - confalte()
 *  3 - collectLatest{}
 */

class MainViewModel : ViewModel() {

    // Холодный поток
    val countDownFlow = flow<Int> {
        val starting = 5
        var currentVal = starting
        emit(currentVal)
        while (currentVal > 0) {
            delay(1000L)
            currentVal--
            emit(currentVal)
        }

    }

    init {
        collectFlow()
    }


    // [[1,2],[2,3],[3,4]]
    // [1,2,2,3,3,4,]
    private fun collectFlow() {
        val flow1 = flow{
            delay(250L)
            emit("first")
            delay(2000L)
            emit("second")
            delay(150L)
            emit("third")
        }
        viewModelScope.launch {
            flow1.onEach {
                println("$it is now")
            }
                .collectLatest{
                println("$it is dilivered")
                delay(1500L)
                println("finised $it")
            }


            /** Примеры терминального использования flow*/
//            filter {  time ->
//                    time % 2 == 0
//                }
//                .map { time->
//                    time*time
//                }
//                .onEach {
//                    println("the current time each $it")
//
//                }
//                .count{
//                    it %2 == 0
//                }
//            val reduseResult = countDownFlow
//                .fold(100) { accumulator, value ->
//                    accumulator + value
//                }
//            println("this count $reduseResult")

        }


    }

}