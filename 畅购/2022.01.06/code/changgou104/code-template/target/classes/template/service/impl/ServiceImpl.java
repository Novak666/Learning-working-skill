package ${package_service_impl};
import ${package_mapper}.${Table}Mapper;
import ${package_pojo}.${Table};
import ${package_service}.${Table}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import com.changgou.core.service.impl.CoreServiceImpl;
import java.util.List;
/****
 * @Author:admin
 * @Description:${Table}业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class ${Table}ServiceImpl extends CoreServiceImpl<${Table}> implements ${Table}Service {

    private ${Table}Mapper ${table}Mapper;

    @Autowired
    public ${Table}ServiceImpl(${Table}Mapper ${table}Mapper) {
        super(${table}Mapper, ${Table}.class);
        this.${table}Mapper = ${table}Mapper;
    }
}
