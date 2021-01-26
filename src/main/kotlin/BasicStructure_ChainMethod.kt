import io.reactivex.rxjava3.core.Flowable

fun main(args: Array<String>) {
    methodChain()
}

/**
 * RxJava 에서 통지하는 데이터를 생성하거나 필터링 또는 변환하는 메소드를 '연산자 (operator)'라고 한다
 *
 * 그리고 이 연산자를 연결해나감으로써 최종 통지 데이터의 단순한 처리를 단계적으로 설정할 수 있다.
 */
fun methodChain() {
    //인자의 데이터를 왼쪽부터 순서대로 통지하는 Flowable 을 just() 메소드로 생성한다
    val flowable: Flowable<Int> = Flowable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        //Flowable 이 통지해주는 데이터를 1건씩 받아 조건에 따라 true/false 값을 리턴한다.
        .filter { data -> data % 2 == 0 }
        //위에서 걸러진 데이터에 100을 곱해 변환한다.
        .map { data -> data * 100 }

    //구독해서 받은 데이터를 출력한다.
    flowable.subscribe { data -> println("data = $data") }
}

