# nativeSample
This projects demonstrates the integration between RN and Android. RN-managed Activity/Fragment is lauched from the drawer to pick up the React component defined in JS.

## How to build
1. Clone the repo
2. Start Android emulator
3. run <code>react-native run-android</code> from project root directory, i.e. directory containing <code>package.json</code> file

## How it works
When Activity with RN content is launched from drawer it creates ReactRootView and builds ReactInstanceManager instance within onCreate() lifecircle method. Then the instance of ReactInstanceManager is passed to ReactRootView object and setContentView() of Activity is called with created ReactRootView object.
The same operations sequence is performed for the launched Fragment. The primary difference is the the point of creation of RN stuff: the fragment starts its communication with RN in its onCreateView() lifecircle method when the Activity performs this in its onCreate() lifecircle.



