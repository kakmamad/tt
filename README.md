

## Tapsell Plugin 

Cordova / PhoneGap Plugin for Tapsell.

## Contents

1. [Description](#description)
2. [Features](#features)
3. [Demo](#quick-demo)
4. [Quick Start](#quick-start)
5. [Installation](#installation)
6. [Usage](#usage)
7. [API](#api)
8. [Wiki and Docs](#wiki-and-docs)
9. [Screenshots](#screenshots)
10. [License](#license)
11. [Credits](#credits)

## Description

This Cordova / PhoneGap plugin enables displaying mobile Ads with single line of javascript code. Designed for the use in HTML5-based cross-platform hybrid games and other applications.

## Features

Platforms supported:
- [ ] Amazon-FireOS, via Android SDK (part of Google Play service)
- [x] Android, via Android SDK (part of Google Play service)
- [ ] iOS
- [ ] Windows Phone

Ad Types:
- [x] Banner
- [x] Interstitial (picture, video), highly recommended. :fire:
- [x] Reward Video, highly recommended. :fire:
- [ ] Native Ads (on roadmap)
- [ ] Native Ads Advanced (on roadmap)

## Quick Demo

Wanna quickly see the mobile ad on your simulator or device? Try the following commands.

```bash
    # install cordova CLI
    [sudo] npm install cordova -g

    # install a small utility to run all the commands for you
    [sudo] npm install plugin-verify -g

    # Demo: run tapsell demo with sample index.html
    plugin-verify tapsell-cordova-plugin
```

## Quick start
```bash
	# create a demo project
    cordova create test1 com.miladesign.masterofadd Test1
    cd test1
    cordova platform add android

    # now add the plugin, cordova CLI will handle dependency automatically
    cordova plugin add tapsell-cordova-plugin

    # now remove the default www content, copy the demo html file to www
    rm -r www/*;
    cp plugins/tapsell-cordova-plugin/test/* www/;

	# now build and run the demo in your device or emulator
    cordova prepare; 
    cordova run android;
    # or import into eclipse
```

## Installation

* If use with Cordova CLI:
```bash
cordova plugin add tapsell-cordova-plugin
```

* If use with PhoneGap Build:
```xml
<plugin name="tapsell-cordova-plugin" source="npm"></plugin>
```

Notice:
* If build locally using ```tapsell-cordova-plugin```, to avoid build error, you need install some extras in Android SDK manager (type ```android sdk``` to launch it):
![android extra](https://cloud.githubusercontent.com/assets/2339512/8176143/20533ec0-1429-11e5-8e17-a748373d5110.png)

## Usage

Show Mobile Ad with single line of javascript code.

Step 1: Create new application, in [Tapsell portal](http://www.tapsell.ir/), then write it in your javascript code.

```javascript
Tapsell.initialize('AppKey');
```

Step 2: Want cheap and basic banner? single line of javascript code.

```javascript
// it will display small banner at top center, using the default options
Tapsell.createBanner(zoneId, Tapsell.AD_POSITION.TOP_CENTER, Tapsell.AD_SIZE.BANNER_320x50);
```

Step 3: Want interstitial Ad to earn more money ? Easy, 2 lines of code. 

```javascript
// preppare and load ad resource in background, e.g. at begining of game level
Tapsell.requestAd(zoneId, Tapsell.CACHE_TYPE.STREAMED);

// show the interstitial later, e.g. at end of game level
Tapsell.showAd(false, false, Tapsell.AD_ROTATION.UNLOCKED, false);
```

## API

Methods:
```javascript
// initialize plugin
initialize(appKey);

// use banner
createBanner(zoneId, position, size);
createBannerAtXY(zoneId, x, y, size);
removeBanner();
showBanner();
hideBanner();

// use interstitial/reward video
requestAd(zoneId, cacheType);
showAd(backDisabled, immersiveMode, rotationMode, showDialog);
```

Events:
```javascript
// onAdAvailable
// onNoAdAvailable
// onError
// onNoNetwork
// onExpiring
// onOpened
// onClosed
// onAdShowFinished
// onRequestFilled
// onHideBannerView
// onShowFailed
document.addEventListener('onAdAvailable', function(e){
    // handle the event
});
```

## Wiki and Docs

Quick start, simply copy & paste:
* [Example Code](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/1.0-Quick-Start-Example-Code)
* [Complete Demo index.html](https://github.com/vinoosir/tapsell-cordova-plugin/blob/master/test/index.html)

API Reference:
* [API Overview](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/1.1-API-Overview)
* [How to Use Banner](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/1.3-Methods-for-Banner)
* [How to Use Interstitial](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/1.4-Methods-for-Interstitial)
* [How to Handle Ad Events](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/1.5-Events)

Other Documentations:
* [ChangeLog](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/ChangeLog)
* [FAQ](https://github.com/vinoosir/tapsell-cordova-plugin/wiki/FAQ)

## Screenshots

Android Banner | Android Interstitial
-------|---------------
![ScreenShot](https://raw.githubusercontent.com/VinoosIr/tapsell-cordova-plugin/master/docs/screenshot_banner.png) | ![ScreenShot](https://raw.githubusercontent.com/VinoosIr/tapsell-cordova-plugin/master/docs/screenshot_interstitial.png)


## License

You can use the plugin for free, or you can also pay to get a license. IMPORTANT!!! Before using the plugin, please read the following content and accept the agreement. THIS WILL AVOID POTENTIAL PROBLEM AND DISPUTE.

There are 3 license options, fully up to you:
1. Free and Open Source, no support
2. Commercial, with email/skype support
3. Win-win partnership, with forum support

## Credits

This project is created and maintained by Milad Mohammadi.

More Cordova/PhoneGap plugins by Milad Mohammadi, [find them in plugin registry](http://plugins.cordova.io/#/search?search=miladesign), or [find them in npm](https://www.npmjs.com/~miladesign).

Project outsourcing and consulting service is also available. Please [contact us](mailto:rezagah.milad@gmail.com) if you have the business needs.