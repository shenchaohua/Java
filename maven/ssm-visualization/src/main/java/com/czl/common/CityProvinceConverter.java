package com.czl.common;

import com.czl.vo.CityVo;
import com.czl.vo.ProvinceVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换器类
 */
public class CityProvinceConverter {

    /**
     * 作用:  cityList 中的招聘数 ===> provinceVoList 中的value值中
     * @param cityList
     * @param provinceVoList
     * @return
     */
    public static List<ProvinceVo> converter(List<CityVo> cityList, List<ProvinceVo> provinceVoList) {
        //cityList  [ {北京  255  }, {上海 300 , 杭州 100} ]

        //provinceVoList  [{浙江  0 [杭州,宁波..]} ,{浙江  0 [杭州,宁波..]}]

        //1.循环省
        flag:   for (ProvinceVo provinceVo : provinceVoList) {
            //统计每一个省的招聘数量
            long sumCount = 0;
            String provinceVoName = provinceVo.getName();
            List<CityVo> pCityList = provinceVo.getCity();
            //用来装省中的城市的名称
            List<String> pCityNameList = new ArrayList<>();
            for (CityVo cityVo : pCityList) {
                //填充城市的名称
                pCityNameList.add(cityVo.getCName());
            }

            //循环cityList
            for (CityVo cityVo : cityList) {
                //判断有些有些省的名称不要是 北京  上海  还有天津 重庆...
                if(provinceVoName.equals("北京") && cityVo.getCName().equals("北京")){
                    //给北京设置招聘人数
                    provinceVo.setValue(cityVo.getNum());
                    continue flag;
                } else if(provinceVoName.equals("上海") && cityVo.getCName().equals("上海")){
                    provinceVo.setValue(cityVo.getNum());
                    continue flag;
                } else if(provinceVoName.equals("天津") && cityVo.getCName().equals("天津")){
                    provinceVo.setValue(cityVo.getNum());
                    continue flag;
                } else if(provinceVoName.equals("重庆") && cityVo.getCName().equals("重庆")){
                    provinceVo.setValue(cityVo.getNum());
                    continue flag;
                } else {
                    //如果省份的名称集合包含城市的名字,累加sumCount 设置给provinceVo
                    if(pCityNameList.contains(cityVo.getCName())){
                        sumCount += cityVo.getNum();
                    }
                }
                provinceVo.setValue(sumCount);
            }

        }
        return provinceVoList;
    }
}
