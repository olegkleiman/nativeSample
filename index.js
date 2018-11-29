import React from 'react';
import MessageQueue from 'react-native/Libraries/BatchedBridge/MessageQueue.js';

import { AppRegistry, StyleSheet, Text, View, Button,
        NativeModules,
        Alert} from 'react-native';
module.exports = NativeModules.ToastAndroid;

var ToastAndroid = require('NativeModules').ToastAndroid;

const spyFunction = (msg) => {
  console.log(msg);
};

MessageQueue.spy(spyFunction);

class App extends React.Component {

  state = {
    ssid: ''
  }

  pickToast() {
    // ToastAndroid.show('Awesome', ToastAndroid.SHORT);
    ToastAndroid.show('Awesome', ToastAndroid.SHORT); //, ToastAndroid.GRAVITY_BOTTOM_KEY);

    // Alert.alert(
    //   'Alert Title',
    //   'My Alert Msg',
    //   [
    //     {text: 'Ask me later', onPress: () => console.log('Ask me later pressed')},
    //     {text: 'Cancel', onPress: () => console.log('Cancel Pressed'), style: 'cancel'},
    //     {text: 'OK', onPress: () => console.log('OK Pressed')},
    //   ],
    //   { cancelable: false }
    // )

  }

  callJSCallback() {

    ToastAndroid.getWifiSSID((message) => {
      this.setState({
        ssid: message
      });
      console.log(message);
    })
  }

  async getFlash() {
    ToastAndroid.hasFlash();
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>Hello from React Native</Text>
        <Button title='Pick toast' onPress={this.pickToast} />
        <Button title='Call JS Callback' onPress={this.callJSCallback} />
        <Text>SSID: {this.state.ssid}</Text>
        <Button title='Get Flash' onPress={this.getFlash}></Button>
      </View>
    );
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('nativeSample', () => App);
