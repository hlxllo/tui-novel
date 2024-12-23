package vip.xiaozhao.intern.baseUtil.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.xiaozhao.intern.baseUtil.intf.mapper.ImageMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper imageMapper;

    @Override
    public void insertImage(String url) {
        imageMapper.insertImage(url);
    }
}
