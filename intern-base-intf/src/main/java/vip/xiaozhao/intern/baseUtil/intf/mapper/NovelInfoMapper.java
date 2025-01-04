package vip.xiaozhao.intern.baseUtil.intf.mapper;

import vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly;
import vip.xiaozhao.intern.baseUtil.intf.entity.NovelInfo;

public interface NovelInfoMapper {

    @ReadOnly
    public NovelInfo getNovelInfoByNovelId(int novelId);
}
