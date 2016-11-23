package com.twkj.lovebook.constants;

import com.twkj.lovebook.bean.BannerBean;
import com.twkj.lovebook.bean.RecommendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiantao on 2016/11/16.
 */

public class BookModelConfig {

    /**
     * bookmodel的小图文件名
     */
    public static List<String> bookModelList = new ArrayList<String>();

    public static List<RecommendBean> recommendBeanList = new ArrayList<RecommendBean>();

    public static List<BannerBean> bannerBeanList = new ArrayList<BannerBean>();

    //我有一句妈卖批不知当讲不当讲
    static{
        bookModelList.add("templatechangpc_0");
        bookModelList.add("templatechangpc_1");
        bookModelList.add("templatechangpc_2");
        bookModelList.add("templatechangpc_3");
        bookModelList.add("templatechangpc_4");
        bookModelList.add("templatechangpc_5");
        bookModelList.add("templatechangpc_6");
        bookModelList.add("templatechangpc_7");
        bookModelList.add("templatechangpc_8");
        bookModelList.add("templatechangpc_9");
        bookModelList.add("templatechangpc_10");
        bookModelList.add("templatechangpc_11");
        bookModelList.add("templatechangpc_12");
        bookModelList.add("templatechangpc_13");
        bookModelList.add("templatechangpc_14");
        bookModelList.add("templatechangpc_15");
        bookModelList.add("templatechangpc_16");
        bookModelList.add("templatechangpc_17");
        bookModelList.add("templatechangpc_18");
        bookModelList.add("templatechangpc_19");
        bookModelList.add("templatechangpc_20");
        bookModelList.add("templatechangpc_21");
        bookModelList.add("templatechangpc_22");
        bookModelList.add("templatechangpc_23");
        bookModelList.add("templatechangpc_24");
        bookModelList.add("templatechangpc_25");
        bookModelList.add("templatechangpc_26");
        bookModelList.add("templatechangpc_27");
        bookModelList.add("templatechangpc_28");
        bookModelList.add("templatechangpc_29");
        bookModelList.add("templatechangpc_30");
        bookModelList.add("templatechangpc_31");
        bookModelList.add("templatechangpc_32");
        bookModelList.add("templatechangpc_33");
        bookModelList.add("templatechangpc_34");
        bookModelList.add("templatechangpc_35");
        bookModelList.add("templatechangpc_36");
        bookModelList.add("templatechangpc_37");
        bookModelList.add("templatechangpc_38");
        bookModelList.add("templatechangpc_39");
        bookModelList.add("templatechangpc_40");
        bookModelList.add("templatechangpc_41");
        bookModelList.add("templatechangpc_42");
        bookModelList.add("templatechangpc_43");
        bookModelList.add("templatechangpc_44");
        bookModelList.add("templatechangpc_45");
        bookModelList.add("templatechangpc_46");
        bookModelList.add("templatechangpc_47");
        bookModelList.add("templatechangpc_48");
        bookModelList.add("templatechangpc_49");
        bookModelList.add("templatechangpc_50");
        bookModelList.add("templatechangpc_51");
        bookModelList.add("templatechangpc_52");
        bookModelList.add("templatechangpc_53");
        bookModelList.add("templatechangpc_54");
        bookModelList.add("templatechangpc_55");
        bookModelList.add("templatechangpc_56");
        bookModelList.add("templatechangpc_57");
        bookModelList.add("templatechangpc_58");
        bookModelList.add("templatechangpc_59");
        bookModelList.add("templatechangpc_60");
        bookModelList.add("templatechangpc_61");
        bookModelList.add("templatechangpc_62");
        bookModelList.add("templatechangpc_63");
        bookModelList.add("templatechangpc_64");
        bookModelList.add("templatechangpc_65");



        RecommendBean rbone = new RecommendBean("摄影" , "img_recommend_takephoto");
        RecommendBean rbtwo = new RecommendBean("旅行" , "img_recommend_trval");
        RecommendBean rbthree = new RecommendBean("生活" , "img_recommend_life");
        recommendBeanList.add(rbone);
        recommendBeanList.add(rbtwo);
        recommendBeanList.add(rbthree);

        BannerBean bbone = new BannerBean(0 , 0 , null , null , "img_reco_first" , null);
        BannerBean bbtwo = new BannerBean(0 , 0 , null , null , "img_reco_second" , null);
        BannerBean bbthree = new BannerBean(0 , 0 , null , null , "img_reco_third" , null);
        bannerBeanList.add(bbone);
        bannerBeanList.add(bbtwo);
        bannerBeanList.add(bbthree);
    }
}
