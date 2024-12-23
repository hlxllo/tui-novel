package vip.xiaozhao.intern.baseUtil.intf.mapper;


import org.apache.ibatis.annotations.Mapper;
import vip.xiaozhao.intern.baseUtil.intf.entity.Image;

@Mapper
public interface ImageMapper {
    Image getImageById(Integer id);

    void insertImage(String url);
}
