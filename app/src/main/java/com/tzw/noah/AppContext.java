package com.tzw.noah;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Environment;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.mob.MobApplication;
import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.NimApplication;
import com.netease.nim.demo.NimDemo;
import com.netease.nim.demo.PrivatizationConfig;
import com.netease.nim.demo.avchat.AVChatProfile;
import com.netease.nim.demo.avchat.activity.AVChatActivity;
import com.netease.nim.demo.avchat.receiver.PhoneCallStateObserver;
import com.netease.nim.demo.common.util.crash.AppCrashHandler;
import com.netease.nim.demo.common.util.sys.SystemUtil;
import com.netease.nim.demo.config.ExtraOptions;
import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.demo.contact.ContactHelper;
import com.netease.nim.demo.event.DemoOnlineStateContentProvider;
import com.netease.nim.demo.event.OnlineStateEventManager;
import com.netease.nim.demo.main.activity.WelcomeActivity;
import com.netease.nim.demo.mixpush.DemoMixPushMessageHandler;
import com.netease.nim.demo.rts.activity.RTSActivity;
import com.netease.nim.demo.session.NimDemoLocationProvider;
import com.netease.nim.demo.session.SessionHelper;
import com.netease.nim.demo.team.TeamAVChatHelper;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.TZWTeamCache;
import com.netease.nim.uikit.cache.TZWUserCache;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nim.uikit.custom.DefaultUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nim.uikit.tzw_relative.GobalObserver;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.ServerAddresses;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.tzw.noah.cache.AppCache;
import com.tzw.noah.db.SnsDBManager;
import com.tzw.noah.init.DBInit;
import com.tzw.noah.init.NimInit;
import com.tzw.noah.logger.Log;
import com.tzw.noah.utils.CrashHandler;
import com.vondear.rxtools.RxTool;

import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by yzy on 2017/6/15.
 */

public class AppContext extends MobApplication { //NimApplication {//Application {

    public static Context instance;

    private static final String TAG = "AppContext";

    public static GobalObserver go;
    //  private Runnable backgroudWorking;
    private static android.os.Handler mdelivery;
    public static boolean loadCompeleted = false;

    protected void attachBaseContext(Context context) {
        printTime(context, "base");
        super.attachBaseContext(context);
        printTime(context, "base");
        MultiDex.install(this);
        printTime(context, "base");

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        instance = getApplicationContext();
//        mdelivery = new android.os.Handler(Looper.getMainLooper());
//        mdelivery.post(new Runnable() {
//            @Override
//            public void run() {

        instance = getApplicationContext();
        Context context = instance;
        RxTool.init(instance);
        printTime(context, "onCreate");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////云信初始化////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DemoCache.setContext(instance);
        printTime(context, "DemoCache");
        // 注册小米推送appID 、appKey 以及在云信管理后台添加的小米推送证书名称，该逻辑放在 NIMClient init 之前
        NIMPushClient.registerMiPush(this, "xiaomi", "2882303761517669332", "5771766994332");
        // 注册自定义小米推送消息处理，这个是可选项
        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        NIMClient.init(instance, getLoginInfo(), getOptions());
        printTime(context, "NIMClient");
        ExtraOptions.provide();
        printTime(context, "ExtraOptions");
        // crash handler
//        AppCrashHandler.getInstance(instance);
        CrashHandler.getInstance().init(instance);
        printTime(context, "CrashHandler");
        if (inMainProcess()) {

            // init pinyin
            PinYin.init(instance);
            PinYin.validate();
            printTime(context, "PinYin");

            // 初始化UIKit模块
            initUIKit();
            printTime(context, "initUIKit");

            // 注册通知消息过滤器
            registerIMMessageFilter();
            printTime(context, "registerIMMessageFilter");

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            printTime(context, "toggleNotification");

            // 注册网络通话来电
//                    registerAVChatIncomingCallObserver(true);

            // 注册白板会话
//                    registerRTSIncomingObserver(true);

            // 注册语言变化监听
            registerLocaleReceiver(true);
            printTime(context, "registerLocaleReceiver");

            OnlineStateEventManager.init();
            printTime(context, "OnlineStateEventManager");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////noah初始化////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        go = new GobalObserverImpl();
        NimUIKit.setGobalObserver(go);
        NimDemo.setGobalObserver(go);
        printTime(context, "setGobalObserver");
        Log.init();
        printTime(context, "Log.init");
//        AppCache.firstInstall();
        new DBInit().systemCacheInit();
        printTime(context, "systemCacheInit");
        new DBInit().snsInit();
        printTime(context, "snsInit");
        NimInit.init(instance);
        printTime(context, "NimInit");
        loadCompeleted = true;
        }
    }

    public static Context getContext() {
        return instance;
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = new DefaultUserInfoProvider(this);

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        // 云信私有化配置项
        configServerAddress(options);

        return options;
    }

    private void configServerAddress(final SDKOptions options) {
        String appKey = PrivatizationConfig.getAppKey();
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey;
        }

        ServerAddresses serverConfig = PrivatizationConfig.getServerAddresses();
        if (serverConfig != null) {
            options.serverConfig = serverConfig;
        }
    }

    private void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();

        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = null;//UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
        } else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // 新增 notificationColor 存储，兼容3.6以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;
            userConfig.notificationFolded = config.notificationFolded;
            userConfig.notificationColor = getResources().getColor(R.color.myRed);
            userConfig.notificationSmallIconId = config.notificationSmallIconId;
        }
        // 持久化生效
        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = MainActivity.class;
        config.notificationSmallIconId = R.drawable.sys_notice_icon;
        config.notificationColor = getResources().getColor(R.color.myRed);
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                android.util.Log.e("Extra", "Extra Message->" + extra);
                if (PhoneCallStateObserver.getInstance().getPhoneCallState() != PhoneCallStateObserver.PhoneCallStateEnum.IDLE
                        || AVChatProfile.getInstance().isAVChatting()
                        || TeamAVChatHelper.sharedInstance().isTeamAVChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0) {
                    LogUtil.i(TAG, "reject incoming call data =" + data.toString() + " as local phone is not idle");
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有网络来电打开AVChatActivity
                AVChatProfile.getInstance().setAVChatting(true);
                AVChatProfile.getInstance().launchActivity(data, AVChatActivity.FROM_BROADCASTRECEIVER);
            }
        }, register);
    }

    private void registerRTSIncomingObserver(boolean register) {
        RTSManager.getInstance().observeIncomingSession(new Observer<RTSData>() {
            @Override
            public void onEvent(RTSData rtsData) {
                RTSActivity.incomingSession(DemoCache.getContext(), rtsData, RTSActivity.FROM_BROADCAST_RECEIVER);
            }
        }, register);
    }

    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };

    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(com.netease.nim.demo.R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(com.netease.nim.demo.R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(com.netease.nim.demo.R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(com.netease.nim.demo.R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(com.netease.nim.demo.R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(com.netease.nim.demo.R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(com.netease.nim.demo.R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(com.netease.nim.demo.R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(com.netease.nim.demo.R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(com.netease.nim.demo.R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(com.netease.nim.demo.R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private MessageNotifierCustomization messageNotifierCustomization = new MessageNotifierCustomization() {
        @Override
        public String makeNotifyContent(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }

        @Override
        public String makeTicker(String nick, IMMessage message) {
            return null; // 采用SDK默认文案
        }
    };


    long firstTime, preSystime = 0;

    private void printTime(Context context, String pre) {
//        if(1==1)
//            return;
        long currentSystime = System.currentTimeMillis();
        long totaltime, interval = 0;
        if (preSystime != 0)
            interval = currentSystime - preSystime;
        else
            firstTime = currentSystime;
        totaltime = currentSystime - firstTime;
        String msg = pre + currentSystime + " " + interval + " " + totaltime;
        preSystime = currentSystime;
        android.util.Log.d("init-time", msg);
    }
}
