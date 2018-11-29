# nativeSample
This project demonstrates the integration between RN and Android. Android Activity/Fragment is launched from the drawer to pick up the React Native component defined in JS. Additionlly, from within RN script, exposed Java method is invoked from NaviteModules JS namespace.

## How to build
1. Clone the repo
2. [Start](https://developer.android.com/studio/run/emulator-commandline) Android emulator
3. run <code>react-native run-android</code> from project root directory, i.e. directory containing <code>package.json</code> file

## How it works
1. <b>Java -> RN.</b> When Activity with RN content is launched it creates ReactRootView and builds ReactInstanceManager instance within onCreate() lifecycle method. Then the instance of ReactInstanceManager is passed to ReactRootView object and setContentView() of Activity is called with created ReactRootView object.
2. <b>Java -> RN.</b> The same operations sequence is performed for the launched Fragment. The primary difference is the the point of creation of RN stuff: the fragment starts its communication with RN in its onCreateView() lifecircle method when the Activity performs this in its onCreate() lifecircle.
3. <b>RN -> Java.</b> With a help of <code>ReactPackage</code> and React module (anyone that inherits from <code>ReactContextBaseJavaModule</code>) Java method decorated by <code>@ReactMethod</code> attribure is exposed to JS context, i.e. made avaiable to JS. Internally this done by Java reflection that investigates the ReactModule and in particular looks for the methods decorated by <code>@ReactMethod</code> attribute. [ReactModuleSpecProcessor](https://github.com/facebook/react-native/blob/42146a7a4ad992a3597e07ead3aafdc36d58ac26/ReactAndroid/src/main/java/com/facebook/react/module/processing/ReactModuleSpecProcessor.java) is in charge of this job.

Generally there are many way to reach the JS Engine context from Java. It seems that RN used <code>j2v8 bindings</code> for this purpose. It's sure that the similar API exists for JavaScriptCore (JSC) Engire as well. Practically RN uses JSC for production and V8 Engine for degugging purposes. 
In the case of RN this exposition is done within a NaviveModule namespace that introduced to JS by 
<code>import { NativeModules } from 'react-native';</code>. That the exposed methid is called as simple as 
<code>
  var ToastAndroid = require('NativeModules').ToastAndroid;
  ToastAndroid.show('Awesome', ToastAndroid.SHORT);
 </code>
Because the dynamic way of the exposition, this call is unsafe meaning there is no way to JS to know in advance the methods or properties added to JS context by <code>ReactPackage</code>.


