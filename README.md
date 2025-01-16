# KMP TUTORIAL

## プロジェクト構造

```
KMPLessonApp/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/     # 共通のKotlinコード
│   │   │   └── kotlin/
│   │   └── iosMain/        # iOS固有のKotlinコード
│   │       └── kotlin/
├── iosApp/                 # iOSアプリケーション
│   └── iosApp/
└── build.gradle.kts        # プロジェクト設定
```

## セットアップ手順

### 1. 環境設定

必要なツール：
- Android Studio
- Xcode 16以降
- JDK 18以降
- Kotlin Multiplatform Mobile (KMM) プラグイン

### 2. プロジェクト作成

1. Android Studioで新規KMPプロジェクトを作成
2. テンプレートから「KMP Application」を選択
3. プロジェクト名と保存場所を設定

### 3. 共通コードの作成

1. `composeApp/src/commonMain/kotlin/`に共通のコードを配置
2. 例：カウンター機能の実装

```kotlin
class Counter {
    private var count: Int = 0

    fun increment() {
        count++
    }

    fun decrement() {
        count--
    }

    fun getCount(): Int {
        return count
    }
}
```

### 4. iOSブリッジの作成

1. `composeApp/src/iosMain/kotlin/`にブリッジコードを配置
2. `@ObjCName`アノテーションを使用してSwiftから呼び出し可能に

```kotlin
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
```

### 5. SwiftUIでの利用

```swift
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
```

## 重要なポイント

1. **共通コード**
   - `commonMain`ディレクトリには、プラットフォーム共通のロジックを配置
   - プラットフォーム固有の機能は使用不可

2. **ブリッジコード**
   - `@ObjCName`アノテーションで、Swiftからアクセスできるようにクラスやメソッドを公開
   - プリミティブ型の変換（Int → NSNumber など）が必要
   - コールバックを使用して状態変更を通知

3. **SwiftUI実装**
   - `@State`を使用して状態を管理
   - `onAppear`でコールバックを設定
   - UIKitではなくSwiftUIを使用してモダンなUI実装

## ビルドと実行

1. Android Studio
   - Gradleビルドを実行
   - `composeApp`モジュールを選択してビルド

2. Xcode
   - `iosApp`ディレクトリをXcodeで開く
   - ビルドターゲットを選択して実行

## トラブルシューティング

1. ブリッジコードが認識されない
   - Xcodeプロジェクトを再ビルド
   - `pod install`を実行

2. 状態更新が反映されない
   - コールバックが正しく設定されているか確認
   - メインスレッドでの更新を確認