# nativeSample
This project demonstrates the integration between RN and Android. Android Activity/Fragment is launched from the drawer to pick up the React Native component defined in JS. Additionlly, from within RN script, exposed Java method is invoked from NaviteModules JS namespace.

## How to build
1. Clone the repo
2. [Start](https://developer.android.com/studio/run/emulator-commandline) Android emulator
3. run <code>react-native run-android</code> from project root directory, i.e. directory containing <code>package.json</code> file

## How it works
1. <b>Java -> RN.</b> When Activity with RN content is launched it creates [ReactRootView](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/ReactRootView.java) and builds [ReactInstanceManager](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/ReactInstanceManager.java) instance within onCreate() lifecycle method. Then the instance of ReactInstanceManager is passed to ReactRootView object and setContentView() of Activity is called with created ReactRootView object:
```java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ReactRootView mReactRootView = new ReactRootView(getActivity());
        ReactInstanceManager mReactInstanceManager;

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(getActivity().getApplication())
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        // The string here (e.g. "MyReactNativeApp") has to match
        // the string in AppRegistry.registerComponent() in index.js
        mReactRootView.startReactApplication(mReactInstanceManager, "nativeSample", null);
        return mReactRootView;
    }
```
2. <b>Java -> RN.</b> The same operations sequence is performed for the launched Fragment. The primary difference is the the point of RN stuff creation: the Fragment starts its communication with RN in its onCreateView() lifecycle method when the Activity performs this in its onCreate() lifecycle.
3. <b>RN -> Java.</b> With a help of [ReactPackage](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/ReactPackage.java) and React module (anyone that inherits from [ReactContextBaseJavaModule](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/bridge/ReactContextBaseJavaModule.java) Java method decorated by <code>@ReactMethod</code> attribure is exposed to JS context, i.e. made avaiable to JS. Internally this done by Java reflection that investigates the ReactModule and in particular looks for the methods decorated by [<code>@ReactMethod</code>](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/bridge/ReactMethod.java)  attribute. [ReactModuleSpecProcessor](https://github.com/facebook/react-native/blob/42146a7a4ad992a3597e07ead3aafdc36d58ac26/ReactAndroid/src/main/java/com/facebook/react/module/processing/ReactModuleSpecProcessor.java) is in charge of this job.

Generally there are many way to reach the JS Engine context from Java. It seems that RN used <code>j2v8 bindings</code> for this purpose. The following is a simplified example of V8 context extending with j2v8 bindings:
```
dependencies {
    compile 'com.eclipsesource.j2v8:j2v8:4.6.0@aar'
}
```
```java
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
...

V8 runtime = V8.createV8Runtime();
runtime.registerJavaMethod(new JavaVoidCallback() {
    public void invoke(final V8Object receiver, final V8Array parameters) {

        if (parameters.length() > 0) {
            jcdemoString(parameters);
        }

    }
}, "call");
...
runtime.release();
```
It's sure that the similar API exists for JavaScriptCore (JSC) Engire as well. Practically RN uses JSC for production and V8 Engine for degugging purposes. 
In the case of RN this exposition is done within a NaviveModule namespace that introduced to JS by 
```javascript
import { NativeModules } from 'react-native';
```
Then the exposed method is called as simple as 
```javascript
var ToastAndroid = require('NativeModules').ToastAndroid;
ToastAndroid.show('Awesome', ToastAndroid.SHORT);
```
Because the dynamic way of the exposition, this call is unsafe meaning there is no way to JS to know in advance the methods or properties added to JS context by [ReactPackage](https://github.com/facebook/react-native/blob/master/ReactAndroid/src/main/java/com/facebook/react/ReactPackage.java).
