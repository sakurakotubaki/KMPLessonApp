import SwiftUI
import ComposeApp

struct ContentView: View {
    @State private var counter = CounterBridge()
    @State private var count: Int = 0
    
    var body: some View {
        VStack(spacing: 20) {
            Text("Count: \(count)")
                .font(.title)
            
            HStack(spacing: 30) {
                Button(action: {
                    counter.decrement()
                }) {
                    Image(systemName: "minus.circle.fill")
                        .font(.system(size: 44))
                }
                
                Button(action: {
                    counter.increment()
                }) {
                    Image(systemName: "plus.circle.fill")
                        .font(.system(size: 44))
                }
            }
        }
        .padding()
        .onAppear {
            count = counter.getCount().intValue
            counter.onCountChanged = { newValue in
                count = newValue.intValue
            }
        }
    }
}
