import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    effectedSample()
}

/**
 * 생산자와 소비자 사이에 이루어지는 처리를 외부 변수를 참조해 바꾸면 비동기 처리에 영향을 준다.
 * 이 메소드는 변수를 참조해 데이터를 받았을 때 처리를 변수(enum 값 참조)에 따라 바꾼다.
 * 실행 결과를 보면 7건을 통지 받는 도중에 계산 방법이 바뀌었다.
 * 만약 덧셈으로만 계산을 쭉 하고나서 계산 방법을 바꾸려고 한 것이라면 의도대로 되지 않는다.
 * 비동기 처리 도중에 변경되는 외부 변수를 참조하므로 의도하지 않는 일이 발생할 수 있다.
 * 그러므로 비동기 처리시에는 생산자에서 소비자까지 처리가 노출되지 않도록 폐쇄적으로 개발하면 위험을 줄일 수 있다.
 */

//계산 방법을 나타내는 enum 객체
private enum class State {
    ADD, MULTIPLY
}

//계산 방법 변수
private var calcMethod: State? = null

fun effectedSample() {

    //계산 방법을 덧셈으로 초기화
    calcMethod = State.ADD

    //300밀리초마다 데이터를 통지하는 Flowable 생성
    val flowable: Flowable<Long> = Flowable.interval(300L, TimeUnit.MILLISECONDS)
        //데이터 7건 통지
        .take(7)
        //각 데이터 calcMethod 타입별 계산. 인자 sum 은 이전까지의 계산 결과이고 data 는 Flowable 로 부터 통지받은 데이터이다.
        .scan { sum, data ->
            if (calcMethod == State.ADD) {
                sum + data
            } else {
                sum * data
            }
        }
    //구독 후 받은 데이터를 출력
    flowable.subscribe { data -> println("data = $data") }

    //잠시 기다렸다가 계산 방법을 곱셈으로 변경
    Thread.sleep(1000)
    println("계산 방법 변경")
    calcMethod = State.MULTIPLY

    Thread.sleep(2000)

}