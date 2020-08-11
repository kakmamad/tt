module.exports = {
	AD_POSITION: {
        TOP_LEFT: 0,
        TOP_CENTER: 1,
		TOP_RIGHT: 2,
		LEFT: 3,
		CENTER: 4,
		RIGHT: 5,
		BOTTOM_LEFT: 6,
		BOTTOM_CENTER: 7,
		BOTTOM_RIGHT: 8
    },
	AD_SIZE: {
        BANNER_320x50: 1,
		BANNER_320x100: 2,
		BANNER_250x250: 3,
		BANNER_300x250: 4
    },
	AD_ROTATION: {
        LOCKED_PORTRAIT: 1,
		LOCKED_LANDSCAPE: 2,
		UNLOCKED: 3,
		LOCKED_REVERSED_LANDSCAPE: 4,
		LOCKED_REVERSED_PORTRAIT: 5
    },
	CACHE_TYPE: {
        CACHED: 1,
		STREAMED: 2
    },
    initialize: function(appKey) {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'initialize',
            [appKey]
        ); 
    },
    createBanner: function(zoneId, position, size) {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'createBanner',
            [ zoneId, position, size ]
        ); 
    },
    createBannerAtXY: function(zoneId, x, y, size) {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'createBannerAtXY',
            [ zoneId, x, y, size ]
        ); 
    },
    removeBanner: function() {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'removeBanner',
            []
        ); 
    },
    showBanner: function() {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'showBanner',
            []
        ); 
    },
    hideBanner: function() {
        cordova.exec(
			null,
			null,
            'Tapsell',
            'hideBanner',
            []
        ); 
    },
    requestAd: function (zoneId, cacheType) {
        var self = this;
        cordova.exec(
            null,
            null,
            'Tapsell',
            'requestAd',
            [ zoneId, cacheType ]
        );
    },
    showAd: function (backDisabled, immersiveMode, rotationMode, showDialog) {
        var self = this;
        cordova.exec(
            null,
            null,
            'Tapsell',
            'showAd',
            [ backDisabled, immersiveMode, rotationMode, showDialog ]
        );
    }
};