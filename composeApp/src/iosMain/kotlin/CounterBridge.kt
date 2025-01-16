import platform.Foundation.NSNumber
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(name = "CounterBridge")
class CounterBridge {
    private val counter = Counter()
    
    @ObjCName(name = "onCountChanged")
    var onCountChanged: ((NSNumber) -> Unit)? = null
    
    fun increment() {
        counter.increment()
        onCountChanged?.invoke(NSNumber(counter.getCount()))
    }
    
    fun decrement() {
        counter.decrement()
        onCountChanged?.invoke(NSNumber(counter.getCount()))
    }
    
    fun getCount(): NSNumber {
        return NSNumber(counter.getCount())
    }
}
