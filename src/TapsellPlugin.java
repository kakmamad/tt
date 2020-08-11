package miladesign.cordova.tapsell;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellRewardListener;
import ir.tapsell.sdk.TapsellShowOptions;
import ir.tapsell.sdk.bannerads.TapsellBannerType;
import ir.tapsell.sdk.bannerads.TapsellBannerView;
import ir.tapsell.sdk.bannerads.TapsellBannerViewEventListener;

public class TapsellPlugin extends CordovaPlugin {
	private static final String LOG_TAG = "TapsellPlugin";
	private static Activity mActivity = null;
	public CordovaInterface cordova = null;
	private FrameLayout bannerLayout;
	private TapsellBannerView banner;
	private TapsellAd tapsellAd;
	private Application _app;
	
	public static final int TOP_LEFT = 0;
	public static final int TOP_CENTER = 1;
	public static final int TOP_RIGHT = 2;
	public static final int LEFT = 3;
	public static final int CENTER = 4;
	public static final int RIGHT = 5;
	public static final int BOTTOM_LEFT = 6;
	public static final int BOTTOM_CENTER = 7;
	public static final int BOTTOM_RIGHT = 8;
	
	@Override
	public void initialize(CordovaInterface initCordova, CordovaWebView webView) {
		 Log.e (LOG_TAG, "initialize");
		 cordova = initCordova;
		 mActivity = cordova.getActivity();
		 _app = cordova.getActivity().getApplication();
		 super.initialize (cordova, webView);
	}
	
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext CallbackContext) throws JSONException {
		if (action.equals("initialize")) {
			String appKey = args.getString(0);
			init(appKey);
			return true;
		}
		if (action.equals("createBanner")) {
			String zoneId = args.getString(0);
			int position = args.getInt(1);
			int size = args.getInt(2);
			createBanner(zoneId, position, size);
			return true;
		}
		if (action.equals("createBannerAtXY")) {
			String zoneId = args.getString(0);
			int x = args.getInt(1);
			int y = args.getInt(2);
			int size = args.getInt(3);
			createBannerAtXY(zoneId, x, y, size);
			return true;
		}
		if (action.equals("removeBanner")) {
			removeBanner();
			return true;
		}
		if (action.equals("showBanner")) {
			showBanner();
			return true;
		}
		if (action.equals("hideBanner")) {
			hideBanner();
			return true;
		}
		if (action.equals("requestAd")) {
			String zoneId = args.getString(0);
			int cacheType = args.getInt(1);
			requestAd(zoneId, cacheType);
		    return true;
		}
		if (action.equals("showAd")) {
			boolean backDisabled = args.getBoolean(0);
			boolean immersiveMode = args.getBoolean(1);
			int rotationMode = args.getInt(2);
			boolean showDialog = args.getBoolean(3);
			showAd(backDisabled, immersiveMode, rotationMode, showDialog);
		    return true;
		}
	    return false;
	}
	
	private void init(String appKey) {
		Tapsell.initialize(_app, appKey);
		Tapsell.setRewardListener(new TapsellRewardListener() {
			@Override
			public void onAdShowFinished(TapsellAd ad, boolean completed) {
				String json = String.format("{'completed':%b, 'isRewardedAd':%b, 'id':'%s'}", new Object[] { completed, ad.isRewardedAd(), ad.getId() });
			    fireEvent("tapsell", "onAdShowFinished", json);
			}
		});
	}
	
	private void createBanner(final String zoneId, final int position, final int size) {
		final TapsellBannerType adSize = TapsellBannerType.fromValue(size);
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (bannerLayout != null) {
					_removeBanner();
				}
				bannerLayout = new FrameLayout(mActivity);
			    banner = new TapsellBannerView(mActivity, adSize, zoneId);
		    	banner.loadAd(mActivity, zoneId, adSize);
				banner.setEventListener(BannerListener);
				if (position == TOP_LEFT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	fLayoutParams.gravity = Gravity.TOP;
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == TOP_CENTER) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					fLayoutParams.gravity = Gravity.TOP;
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = Gravity.CENTER;
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == TOP_RIGHT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			    	fLayoutParams.gravity = Gravity.TOP;
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = Gravity.RIGHT;
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == LEFT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = (Gravity.CENTER_VERTICAL | Gravity.LEFT);
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == CENTER) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = Gravity.CENTER;
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == RIGHT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = (Gravity.CENTER_VERTICAL | Gravity.RIGHT);
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == BOTTOM_LEFT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	fLayoutParams.gravity = Gravity.BOTTOM;
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				} else if (position == BOTTOM_CENTER) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			    	fLayoutParams.gravity = Gravity.BOTTOM;
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = Gravity.CENTER;
			    	banner.setLayoutParams(params);
				    bannerLayout.addView(banner);
					Log.e(LOG_TAG, "HERE");
				} else if (position == BOTTOM_RIGHT) {
					FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			    	fLayoutParams.gravity = Gravity.BOTTOM;
			    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    	params.gravity = Gravity.RIGHT;
			    	banner.setLayoutParams(params);
			    	bannerLayout.setLayoutParams(fLayoutParams);
				    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
				    bannerLayout.addView(banner);
				}
			}
		});
	}
	
	private void createBannerAtXY(final String zoneId, final int x, final int y, final int size) {
		final TapsellBannerType adSize = TapsellBannerType.fromValue(size);
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (bannerLayout != null) {
					_removeBanner();
				}
				bannerLayout = new FrameLayout(mActivity);
			    FrameLayout.LayoutParams fLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			    fLayoutParams.leftMargin = x;
		    	fLayoutParams.topMargin = y;
			    bannerLayout.setLayoutParams(fLayoutParams);
			    ((ViewGroup) getParentGroup().getParent()).addView(bannerLayout, 1);
			    banner = new TapsellBannerView(mActivity, adSize, zoneId);
		    	banner.loadAd(mActivity, zoneId, adSize);
				banner.setEventListener(BannerListener);
			    bannerLayout.addView(banner);
			}
		});
	}

	private void removeBanner() {
		if (bannerLayout == null)
		      return;
	    if (mActivity != null) {
	    	mActivity.runOnUiThread(new Runnable() {
	        public void run() {
	        	ViewGroup viewGroup;
      		if (((viewGroup = getParentGroup()) != null) && ((viewGroup instanceof ViewGroup)) && (((ViewGroup)viewGroup.getParent()).getChildAt(1) != null))
      			((ViewGroup)viewGroup.getParent()).removeViewAt(1);
	        }
	      });
	    }
	}
	
	private void _removeBanner() {
		if (bannerLayout == null)
		      return;
	    if (mActivity != null) {
	    	mActivity.runOnUiThread(new Runnable() {
		        public void run() {
		        	ViewGroup viewGroup;
	        		if (((viewGroup = getParentGroup()) != null) && ((viewGroup instanceof ViewGroup)) && (((ViewGroup)viewGroup.getParent()).getChildAt(1) != null))
	        			((ViewGroup)viewGroup.getParent()).removeViewAt(1);
		        }
	    	});
	    }
	}

	private void showBanner() {
		try {
			if (mActivity != null) {
		    	mActivity.runOnUiThread(new Runnable() {
			        public void run() {
						banner.showBannerView();
						bannerLayout.setVisibility(View.VISIBLE);
			        }
		    	});
		    }
		} catch(Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}

	private void hideBanner() {
		try {
			if (mActivity != null) {
		    	mActivity.runOnUiThread(new Runnable() {
			        public void run() {
						banner.hideBannerView();
						bannerLayout.setVisibility(View.INVISIBLE);
			        }
		    	});
		    }
		} catch(Exception e) {
			Log.e(LOG_TAG, e.getMessage());
		}
	}
	
	private ViewGroup getParentGroup() {
	    try {
	      return (ViewGroup)this.webView.getClass().getMethod("getView", new Class[0]).invoke(this.webView, new Object[0]);
	    } catch (Exception ex) {
	    	try {
	    		return (ViewGroup)this.webView.getClass().getMethod("getParent", new Class[0]).invoke(this.webView, new Object[0]);
	    	} catch (Exception e) {
	    		e.printStackTrace(); 
	        }
	    }
	    return null;
	}
	
	private void requestAd(String zoneId, int cacheType) throws JSONException {
		if(zoneId!=null && (zoneId.equalsIgnoreCase("null") || zoneId.equalsIgnoreCase(""))) {
			zoneId = null;
		}
		TapsellAdRequestOptions options = new TapsellAdRequestOptions();
        options.setCacheType(cacheType);
		Tapsell.requestAd(mActivity, zoneId, options, new TapsellAdRequestListener() {

			@Override
			public void onError(String message) {
				tapsellAd = null;
				String json = String.format("{'adType':'%s', 'message':'%s'}", new Object[] { "nonBanner", message });
			    fireEvent("tapsell", "onError", json);
			}

			@Override
			public void onAdAvailable(TapsellAd ad) {
				tapsellAd = ad;
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onAdAvailable", json);
			}

			@Override
			public void onNoAdAvailable() {
				tapsellAd = null;
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onNoAdAvailable", json);
			}

			@Override
			public void onNoNetwork() {
				tapsellAd = null;
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onNoNetwork", json);
			}

			@Override
			public void onExpiring(TapsellAd ad) {
				tapsellAd = ad;
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onExpiring", json);
			}
		});
		
	}
	
	private void showAd(boolean backDisabled, boolean immersiveMode, int rotationMode, boolean showDialog) {
		if (tapsellAd == null) {
			String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
		    fireEvent("tapsell", "onShowFailed", json);
            return;
        }
		TapsellShowOptions showOptions = new TapsellShowOptions();
        showOptions.setBackDisabled(backDisabled);
        showOptions.setImmersiveMode(immersiveMode);
        showOptions.setRotationMode(rotationMode);
        showOptions.setShowDialog(showDialog);
		tapsellAd.show(mActivity, showOptions, new TapsellAdShowListener() {
		    @Override
		    public void onOpened(TapsellAd ad) {
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onOpened", json);
		    }
		    @Override
		    public void onClosed(TapsellAd ad) {
				String json = String.format("{'adType':'%s'}", new Object[] { "nonBanner" });
			    fireEvent("tapsell", "onClosed", json);
		    }
		});
	}
	
	private TapsellBannerViewEventListener BannerListener = new TapsellBannerViewEventListener(){

		@Override
		public void onNoAdAvailable() {
			String json = String.format("{'adType':'%s'}", new Object[] { "banner" });
		    fireEvent("tapsell", "onNoAdAvailable", json);
		}

		@Override
		public void onNoNetwork() {
			String json = String.format("{'adType':'%s'}", new Object[] { "banner" });
		    fireEvent("tapsell", "onNoNetwork", json);
		}

		@Override
		public void onError(String message) {
			String json = String.format("{'adType':'%s', 'message':'%s'}", new Object[] { "banner", message });
		    fireEvent("tapsell", "onError", json);
		}

		@Override
		public void onRequestFilled() {
			String json = String.format("{'adType':'%s'}", new Object[] { "banner" });
		    fireEvent("tapsell", "onRequestFilled", json);
		}

		@Override
		public void onHideBannerView() {
			String json = String.format("{'adType':'%s'}", new Object[] { "banner" });
		    fireEvent("tapsell", "onHideBannerView", json);
		}
	};
	
	public void fireEvent(String obj, String eventName, String jsonData) {
			String js;
			if("window".equals(obj)) {
				js = "var evt=document.createEvent('UIEvents');evt.initUIEvent('" + eventName + "',true,false,window,0);window.dispatchEvent(evt);";
			} else {
				js = "javascript:cordova.fireDocumentEvent('" + eventName + "'";
				if(jsonData != null) {
					js += "," + jsonData;
				}
				js += ");";
			}
			webView.loadUrl(js);
	}
}