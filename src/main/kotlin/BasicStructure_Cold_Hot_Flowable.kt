import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.flowables.ConnectableFlowable
import java.util.concurrent.ThreadLocalRandom


/**
 * Cold 생산자는 1개의 소비자와 구독 관계를 맺지만, Hot 생산자는 여러 소비자와 구독 관계를 맺을 수 있다.
 *
 * Cold 생산자를 구독하면 생산자 처리가 시작되지만, Hot 생산자는 구독해도 생산자 처리가 시작되지 않을 수 있다.
 *
 * RxJava 에서 생성 메소드로 생성된 생산자는 '기본적으로 Cold 생산자'이고, Hot 생산자를 생성하려면 Cold 생산자에서 Hot 생산자로 변환하는 메소드를 호출하거나 Processor 와 Subject 를 생성해야 한다.
 *
 * ConnectableFlowable/ConnectableObservable 은 Hot 생산자이다. 또한 subscribe() 만 실행한다고 해서 시작하지 않고 connect() 를 실행해야 처리를 시작한다.
 *
 * 그러므로 처리를 시작하기 전에 필요한 Subscriber 을 만들어 구독하게 하고 이후에 처리를 시작해야 정상적으로 동작한다.
 *
 * refCount() - Hot 생산자인 ConnectableFlowable/ConnectableObservable 에서 새로운 Flowable/Observable 을 생성한다.
 *              이랗게 생성된 Flowable/Observable은 Cold 이므로 connect 메소드가 따로 없다.
 *
 * autoConnect() - ConnectableFlowable/ConnectableObservable 에서 지정한 개수의 구독이 시작된 시점에 처리를 시작하는 Flowable/Observable 을 생성한다.
 *                 만약 autoConnect() 메소드 안에 인자 없이 생성하면 처음 subscribe 메소드가 호출된 시점에 처리를 시작하고, 개수를 지정해주면 지정 개수에 도달한 시점에서 처리를 시작한다.
 *                 또한 autoConnect()로 생성한 Flowable/Observable 은 처리가 완료된 뒤 또는 모든 구독이 해지된 뒤에는 다시 subscribe 메소드를 호출해도 다시 처리가 되진 않는다.
 */
fun main(args: Array<String>){

    makeHotFlowable()

}

fun makeHotFlowable(){
    val connectable = Flowable.just("")
        .map { println("map") }
        .replay(1)

    //refCount()
    connectable.publish().refCount().subscribe{x-> println(x)}
    println("connect")


    //autoConnect()
    val source = Observable.range(1, 3)
        .map{x -> ThreadLocalRandom.current().nextInt(1, 100)}
        .publish()
        .autoConnect(2)

    source.subscribe { x -> println(x) }
    source.subscribe { x -> println(x) }

}