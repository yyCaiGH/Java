package com.up.infant.controller.goods;

import com.up.common.utils.Logger;
import com.up.common.utils.TextUtils;
import com.up.infant.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/11/4 0004. 21:29
 */
public class GoodsLogic {

    /**
     * 初始化添加商品的属性
     * 1、一级分类
     * 2、品牌
     * 3、年龄段
     * 4、性别
     * 5、季节
     * 6、产地-国家
     * 7、商品tag
     * 8、SKU设置
     * 9、运费组
     */
    public Map<String,Object> getGoodsAttr() {
        //1、一级分类
        List<Category> oneGoodsClassList = Category.dao.getList(0);
        //2、品牌
        List<Brand> brandList = Brand.dao.getPutawayBrands();
        //3、年龄段
        List<SysConfig> ageList=SysConfig.dao.getConfigList(5);
        //4、性别
        List<SysConfig> sexList=SysConfig.dao.getConfigList(8);
        //5、季节
        List<SysConfig> seasonList=SysConfig.dao.getConfigList(6);
        //6、产地-国家
        List<Country> countryList = Country.dao.getList();
        //7、商品tag
        List<GoodsTag> goodsTagList = GoodsTag.dao.getList();
        //8、SKU设置
        Map<String,Object> skuList = Sku.dao.getListAll();
        //9、运费组
        List<PostageGroup> postageGroupList = PostageGroup.dao.getList();
        //10、顺便带上sku的父级列表
        List<Sku> parentSkuList = Sku.dao.getList(0);

        Map<String,Object> data = new HashMap<String ,Object>();
        data.put("oneGoodsClassList",oneGoodsClassList);
        data.put("brandList",brandList);
        data.put("ageList",ageList);
        data.put("sexList",sexList);
        data.put("seasonList",seasonList);
        data.put("countryList",countryList);
        data.put("goodsTagList",goodsTagList);
        data.put("skuList",skuList);
        data.put("postageGroupList",postageGroupList);
        data.put("parentSkuList",parentSkuList);
        return data;
    }

    public Map<String,Object> getEditGoodsAttr(int goodsId) {
        Map<String,Object> map = getGoodsAttr();
        //商品sku
        SkuGoods skuGoods = new SkuGoods();
        skuGoods.setGoodsId(goodsId);
        List<SkuGoods> skuGoodslist = SkuGoods.dao.getList(skuGoods);
        map.put("skuGoodslist",skuGoodslist);
        //商品卖点
        GoodsPoints goodsPoints = new GoodsPoints();
        goodsPoints.setGoodsId(goodsId);
        List<GoodsPoints> goodsPointsList = GoodsPoints.dao.getList(goodsPoints);
        map.put("goodsPointsList",goodsPointsList);
        //商品信息
        Goods goods =  Goods.dao.getById(goodsId);
        map.put("goods",goods);
        return map;
    }
    public Map<String,Object> getGoodsSearchInfo() {
        //1、一级分类
        List<Category> oneGoodsClassList = Category.dao.getList(0);
        //2、品牌
        List<Brand> brandList = Brand.dao.getPutawayBrands();

        Map<String,Object> data = new HashMap<String ,Object>();
        data.put("oneGoodsClassList",oneGoodsClassList);
        data.put("brandList",brandList);
        return data;
    }

    public int addGoods(String goodsSkuListJsonStr, String goodsJsonStr, String goodsSellPointJsonStr) {
        //添加主商品
        JSONObject goodsJson = (JSONObject)JSONValue.parse(goodsJsonStr);
        Goods goods = new Goods();
        try{
            String one_cat_id = goodsJson.get("one_cat_id").toString();
            goods.setOneCatId(Integer.parseInt(one_cat_id));
            String tow_cat_id = goodsJson.get("tow_cat_id").toString();
            goods.setTowCatId(Integer.parseInt(tow_cat_id));
            String brand_id = goodsJson.get("brand_id").toString();
            goods.setBrandId(Integer.parseInt(brand_id));
            String main_number = goodsJson.get("main_number").toString();
            goods.setMainNumber(main_number);
            String name = goodsJson.get("name").toString();
            goods.setName(name);
            String market_price = goodsJson.get("market_price").toString();
            goods.setMarketPrice(Double.parseDouble(market_price));
            String promote_price = goodsJson.get("promote_price").toString();
            goods.setPromotePrice(Double.parseDouble(promote_price));
            String apply_sex = goodsJson.get("apply_sex").toString();
            goods.setApplySex(Integer.parseInt(apply_sex));
            String apply_age = goodsJson.get("apply_age").toString();
            goods.setApplyAge(apply_age);
            String apply_season = goodsJson.get("apply_season").toString();
            goods.setApplySeason(apply_season);
            String texture = goodsJson.get("texture").toString();
            goods.setTexture(texture);
            String img1_url = goodsJson.get("img1_url").toString();
            goods.setImg1Url(img1_url);
            String img2_url = goodsJson.get("img2_url").toString();
            goods.setImg2Url(img2_url);
            String img3_url = goodsJson.get("img3_url").toString();
            goods.setImg3Url(img3_url);
            String img4_url = goodsJson.get("img4_url").toString();
            goods.setImg4Url(img4_url);
            String img5_url = goodsJson.get("img5_url").toString();
            goods.setImg5Url(img5_url);
            String tag_id = goodsJson.get("tag_id").toString();
            goods.setTagId(Integer.parseInt(tag_id));
            String production_area = goodsJson.get("production_area").toString();
            goods.setProductionArea(production_area);
            String describe = goodsJson.get("describe").toString();
            goods.setDescribe(describe);
            String postage = goodsJson.get("postage").toString();
            goods.setPostage(Integer.parseInt(postage));
            String packing_list = goodsJson.get("packing_list").toString();
            goods.setPackingList(packing_list);
            String after_sales = goodsJson.get("after_sales").toString();
            goods.setAfterSales(after_sales);
            String putaway = goodsJson.get("putaway").toString();
            goods.setPutaway(Integer.parseInt(putaway));
            goods.setCreateTime(new Date());
            if (Integer.parseInt(putaway)==1){
                goods.setPutawayTime(new Date());
            }
            else{
                goods.setSoldoutTime(new Date());
            }
            goods.save();
            Brand.dao.updateBrandPutawayCount(goods.getBrandId(),goods.getPutaway());

        }catch (Exception e){
            return -1;
        }

        int goodsId = goods.getId();

        try{
            //批量添加商品的sku
            JSONArray array=(JSONArray)JSONValue.parse(goodsSkuListJsonStr);
            List<SkuGoods> skuList = new ArrayList<SkuGoods>();
            int goodsRepertory = 0;//主商品库存
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                SkuGoods newSku = new SkuGoods();
                newSku.setGoodsId(goodsId);
                String sku1_id = json.get("sku1_id").toString();
                if (TextUtils.isInteger(sku1_id)){
                    newSku.setSku1Id(Integer.parseInt(sku1_id));
                }
                String sku2_id = json.get("sku2_id").toString();
                if (TextUtils.isInteger(sku2_id)){
                    newSku.setSku2Id(Integer.parseInt(sku2_id));
                }
                String sku3_id = json.get("sku3_id").toString();
                if (TextUtils.isInteger(sku3_id)){
                    newSku.setSku3Id(Integer.parseInt(sku3_id));
                }
                String market_price = json.get("market_price").toString();
                if (TextUtils.isDouble(market_price)){
                    newSku.setMarketPrice(Double.parseDouble(market_price));
                }
                String promote_price = json.get("promote_price").toString();
                if (TextUtils.isDouble(promote_price)){
                    newSku.setPromotePrice(Double.parseDouble(promote_price));
                }
                String repertory = json.get("repertory").toString();
                if (TextUtils.isInteger(repertory)){
                    newSku.setRepertory(Integer.parseInt(repertory));
                    goodsRepertory+=Integer.parseInt(repertory);
                }
                String goods_number = json.get("goods_number").toString();
                newSku.setGoodsNumber(goods_number);
                String putaway1 = json.get("putaway").toString();
                if (TextUtils.isInteger(putaway1)){
                    newSku.setPutaway(Integer.parseInt(putaway1));
                }
                skuList.add(newSku);
            }
            SkuGoods.dao.addBatch(skuList);
            goods.setSkuRepertory(goodsRepertory);
            goods.update();//更新主商品库存
            //Brand.dao.updateSkuRepertory(true,goods.getBrandId(),goodsRepertory);更新商品的时候不能直接对品牌添加库存，所以品牌的库存只能查询获取，
            //或者重新获取该品牌下的商品的库存，然后更新操作
        }catch (Exception e){
            return -2;
        }

        try{
            //批量添加商品的卖点
            JSONArray array1=(JSONArray)JSONValue.parse(goodsSellPointJsonStr);
            List<GoodsPoints> goodsPointsesList = new ArrayList<GoodsPoints>();
            for (int i = 0 ;i<array1.size();i++){
                JSONObject json=(JSONObject)array1.get(i);
                GoodsPoints newGp = new GoodsPoints();
                newGp.setGoodsId(goodsId);
                String sell_point = json.get("sell_point").toString();
                newGp.setSellPoint(sell_point);
                goodsPointsesList.add(newGp);
            }
            GoodsPoints.dao.addBatch(goodsPointsesList);
        }catch (Exception e){
            return -3;
        }

        return 0;
    }

    public int updateGoods(String goodsSkuListJsonStr, String goodsJsonStr, String goodsSellPointJsonStr) {
        //更新主商品
        JSONObject goodsJson = (JSONObject)JSONValue.parse(goodsJsonStr);
        Goods goods = new Goods();
        try{
            String id = goodsJson.get("id").toString();
            goods.setId(Integer.parseInt(id));
            String one_cat_id = goodsJson.get("one_cat_id").toString();
            goods.setOneCatId(Integer.parseInt(one_cat_id));
            String tow_cat_id = goodsJson.get("tow_cat_id").toString();
            goods.setTowCatId(Integer.parseInt(tow_cat_id));
            String brand_id = goodsJson.get("brand_id").toString();
            goods.setBrandId(Integer.parseInt(brand_id));
            String main_number = goodsJson.get("main_number").toString();
            goods.setMainNumber(main_number);
            String name = goodsJson.get("name").toString();
            goods.setName(name);
            String market_price = goodsJson.get("market_price").toString();
            goods.setMarketPrice(Double.parseDouble(market_price));
            String promote_price = goodsJson.get("promote_price").toString();
            goods.setPromotePrice(Double.parseDouble(promote_price));
            String apply_sex = goodsJson.get("apply_sex").toString();
            goods.setApplySex(Integer.parseInt(apply_sex));
            String apply_age = goodsJson.get("apply_age").toString();
            goods.setApplyAge(apply_age);
            String apply_season = goodsJson.get("apply_season").toString();
            goods.setApplySeason(apply_season);
            String texture = goodsJson.get("texture").toString();
            goods.setTexture(texture);
            String img1_url = goodsJson.get("img1_url").toString();
            goods.setImg1Url(img1_url);
            String img2_url = goodsJson.get("img2_url").toString();
            goods.setImg2Url(img2_url);
            String img3_url = goodsJson.get("img3_url").toString();
            goods.setImg3Url(img3_url);
            String img4_url = goodsJson.get("img4_url").toString();
            goods.setImg4Url(img4_url);
            String img5_url = goodsJson.get("img5_url").toString();
            goods.setImg5Url(img5_url);
            String tag_id = goodsJson.get("tag_id").toString();
            goods.setTagId(Integer.parseInt(tag_id));
            String production_area = goodsJson.get("production_area").toString();
            goods.setProductionArea(production_area);
            String describe = goodsJson.get("describe").toString();
            goods.setDescribe(describe);
            String postage = goodsJson.get("postage").toString();
            goods.setPostage(Integer.parseInt(postage));
            String packing_list = goodsJson.get("packing_list").toString();
            goods.setPackingList(packing_list);
            String after_sales = goodsJson.get("after_sales").toString();
            goods.setAfterSales(after_sales);
            String putaway = goodsJson.get("putaway").toString();
            goods.setPutaway(Integer.parseInt(putaway));
            if (Integer.parseInt(putaway)==1){
                goods.setPutawayTime(new Date());
            }
            else{
                goods.setSoldoutTime(new Date());
            }
            goods.update();
            Brand.dao.updateBrandPutawayCount(goods.getBrandId(),goods.getPutaway());

        }catch (Exception e){
            return -1;
        }

        int goodsId = goods.getId();

        try{
            //更新商品的sku，先批量下架所有已有的sku商品，然后将有id的更新，没id的添加

            //1、批量下架所有的已存在的sku商品
            List<SkuGoods> skuList = SkuGoods.dao.getList(goodsId);
            for (int i = 0;i<skuList.size();i++){
                skuList.get(i).setPutaway(2);
            }
            SkuGoods.dao.updateBatch(skuList);
            //将有id的商品更新，没id的商品添加
            JSONArray array=(JSONArray)JSONValue.parse(goodsSkuListJsonStr);
            int goodsRepertory = 0;//主商品库存
            for (int i = 0 ;i<array.size();i++){
                JSONObject json=(JSONObject)array.get(i);
                SkuGoods newSku = new SkuGoods();
                newSku.setGoodsId(goodsId);
                String id = json.get("id").toString();
                if (TextUtils.isInteger(id)){
                    newSku.setId(Integer.parseInt(id));
                }
                String sku1_id = json.get("sku1_id").toString();
                if (TextUtils.isInteger(sku1_id)){
                    newSku.setSku1Id(Integer.parseInt(sku1_id));
                }
                String sku2_id = json.get("sku2_id").toString();
                if (TextUtils.isInteger(sku2_id)){
                    newSku.setSku2Id(Integer.parseInt(sku2_id));
                }
                String sku3_id = json.get("sku3_id").toString();
                if (TextUtils.isInteger(sku3_id)){
                    newSku.setSku3Id(Integer.parseInt(sku3_id));
                }
                String market_price = json.get("market_price").toString();
                if (TextUtils.isDouble(market_price)){
                    newSku.setMarketPrice(Double.parseDouble(market_price));
                }
                String promote_price = json.get("promote_price").toString();
                if (TextUtils.isDouble(promote_price)){
                    newSku.setPromotePrice(Double.parseDouble(promote_price));
                }
                String repertory = json.get("repertory").toString();
                if (TextUtils.isInteger(repertory)){
                    newSku.setRepertory(Integer.parseInt(repertory));
                    goodsRepertory+=Integer.parseInt(repertory);
                }
                String goods_number = json.get("goods_number").toString();
                newSku.setGoodsNumber(goods_number);
                String putaway1 = json.get("putaway").toString();
                if (TextUtils.isInteger(putaway1)){
                    newSku.setPutaway(Integer.parseInt(putaway1));
                }
                if (newSku.getId()==null){//添加
                    newSku.save();
                }
                else{//更新
                    newSku.update();
                }
            }
            goods.setSkuRepertory(goodsRepertory);
            goods.update();//更新主商品库存

            if (goods.getPutaway()==2){//主商品下架，所属sku商品也要下架
                List<SkuGoods> skuGoodsList = SkuGoods.dao.getList(goods.getId());
                for (int j = 0;j<skuGoodsList.size();j++){
                    skuGoodsList.get(j).setPutaway(2);
                }
                SkuGoods.dao.updateBatch(skuGoodsList);
            }
        }catch (Exception e){
            return -2;
        }

        try{
            //批量删除，再批量添加
            GoodsPoints.dao.deleteBatch(goodsId);

            JSONArray array1=(JSONArray)JSONValue.parse(goodsSellPointJsonStr);
            List<GoodsPoints> goodsPointsesList = new ArrayList<GoodsPoints>();
            for (int i = 0 ;i<array1.size();i++){
                JSONObject json=(JSONObject)array1.get(i);
                GoodsPoints newGp = new GoodsPoints();
                newGp.setGoodsId(goodsId);
                String sell_point = json.get("sell_point").toString();
                newGp.setSellPoint(sell_point);
                goodsPointsesList.add(newGp);
            }
            GoodsPoints.dao.addBatch(goodsPointsesList);
        }catch (Exception e){
            return -3;
        }

        return 0;
    }
}
