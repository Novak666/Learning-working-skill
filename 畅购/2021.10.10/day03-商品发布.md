# 学习目标

- SPU与SKU概念理解

  ```
  SPU：某一款商品的公共属性
  SKU:某款商品的不同参数对应的商品信息[某个商品]
  ```

- ==新增商品、修改商品==

  ```
  增加：增加SPU和SKU
  修改：修改SPU和SKU
  ```

- 商品审核、上架、下架

  ```
  审核：修改审核状态
  上架下架：修改上架下架状态
  ```

- 删除商品

  ```
  逻辑删除：修改了删除状态
  物理删除：真实删除了数据
  ```

- 找回商品

  ```
  找回商品：一定是属于逻辑删除的商品
  ```

# 1. SPU与SKU

## 1.1 SPU与SKU概念

**SPU = Standard Product Unit  （标准产品单位）**

* 概念 : SPU 是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。

* 通俗点讲，属性值、特性相同的货品就可以称为一个 SPU

  ==同款商品的公共属性抽取==

  例如：**华为P30 就是一个 SPU**

**SKU=stock keeping unit( 库存量单位)**

* SKU 即库存进出计量的单位， 可以是以件、盒、托盘等为单位。

* SKU 是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。

* 在服装、鞋类商品中使用最多最普遍。

  例如：**华为P30 红色 64G 就是一个 SKU**
  
  ==某个库存单位的商品独有属性(某个商品的独有属性)==

## 1.2 表结构分析

tb_spu  表 （SPU表）

| 字段名称           | 字段含义   | 字段类型    | 字段长度 | 备注   |
| -------------- | ------ | ------- | ---- | ---- |
| id             | 主键     | BIGINT  |      |      |
| sn             | 货号     | VARCHAR |      |      |
| name           | SPU名   | VARCHAR |      |      |
| caption        | 副标题    | VARCHAR |      |      |
| brand_id       | 品牌ID   | INT     |      |      |
| category1_id   | 一级分类   | INT     |      |      |
| category2_id   | 二级分类   | INT     |      |      |
| category3_id   | 三级分类   | INT     |      |      |
| template_id    | 模板ID   | INT     |      |      |
| freight_id     | 运费模板id | INT     |      |      |
| image          | 图片     | VARCHAR |      |      |
| images         | 图片列表   | VARCHAR |      |      |
| sale_service   | 售后服务   | VARCHAR |      |      |
| introduction   | 介绍     | TEXT    |      |      |
| spec_items     | 规格列表   | VARCHAR |      |      |
| para_items     | 参数列表   | VARCHAR |      |      |
| sale_num       | 销量     | INT     |      |      |
| comment_num    | 评论数    | INT     |      |      |
| is_marketable  | 是否上架   | CHAR    |      |      |
| is_enable_spec | 是否启用规格 | CHAR    |      |      |
| is_delete      | 是否删除   | CHAR    |      |      |
| status         | 审核状态   | CHAR    |      |      |

tb_sku  表（SKU商品表）

| 字段名称          | 字段含义                | 字段类型     | 字段长度 | 备注   |
| ------------- | ------------------- | -------- | ---- | ---- |
| id            | 商品id                | BIGINT   |      |      |
| sn            | 商品条码                | VARCHAR  |      |      |
| name          | SKU名称               | VARCHAR  |      |      |
| price         | 价格（分）               | INT      |      |      |
| num           | 库存数量                | INT      |      |      |
| alert_num     | 库存预警数量              | INT      |      |      |
| image         | 商品图片                | VARCHAR  |      |      |
| images        | 商品图片列表              | VARCHAR  |      |      |
| weight        | 重量（克）               | INT      |      |      |
| create_time   | 创建时间                | DATETIME |      |      |
| update_time   | 更新时间                | DATETIME |      |      |
| spu_id        | SPUID               | BIGINT   |      |      |
| category_id   | 类目ID                | INT      |      |      |
| category_name | 类目名称                | VARCHAR  |      |      |
| brand_name    | 品牌名称                | VARCHAR  |      |      |
| spec          | 规格                  | VARCHAR  |      |      |
| sale_num      | 销量                  | INT      |      |      |
| comment_num   | 评论数                 | INT      |      |      |
| status        | 商品状态 1-正常，2-下架，3-删除 | CHAR     |      |      |

# 2. 新增和修改商品 

## 2.1 需求分析 

实现商品的新增与修改功能。

(1)第1个步骤，先选择添加的商品所属分类

![1](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/1.png)

这块在第2天的代码中已经有一个根据父节点ID查询分类信息的方法，参考第2天的4.3.4的findByPrantId方法，首先查询顶级分类，也就是pid=0，然后根据用户选择的分类，将选择的分类作为pid查询子分类。

（2)第2个步骤，填写SPU的信息

![2](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/2.png)

(3)第3个步骤，填写SKU信息

![3](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/3.png)

先进入选择商品分类 再填写商品的信息 填写商品的属性添加商品。

## 2.2 实现思路 

前端传递给后端的数据格式 是一个spu对象和sku列表组成的对象,如下图：

![4](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/4.png)

上图JSON数据如下：

```json
{
  "spu": {
    "name": "这个是商品名称",
    "caption": "这个是副标题",
    "brandId": 12,
    "category1Id": 558,
    "category2Id": 559,
    "category3Id": 560,
    "freightId": 10,
    "image": "http://www.qingcheng.com/image/1.jpg",
    "images": "http://www.qingcheng.com/image/1.jpg,http://www.qingcheng.com/image/2.jpg",
    "introduction": "这个是商品详情，html代码",
    "paraItems": {
      "出厂年份": "2019",
      "赠品": "充电器"
    },
    "saleService": "七天包退,闪电退货",
    "sn": "020102331",
    "specItems": {
      "颜色": [
        "红",
        "绿"
      ],
      "机身内存": [
        "64G",
        "8G"
      ]
    },
    "templateId": 42
  },
  "skuList": [
    {
      "sn": "10192010292",
      "num": 100,
      "alertNum": 20,
      "price": 900000,
      "spec": {
        "颜色": "红",
        "机身内存": "64G"
      },
      "image": "http://www.qingcheng.com/image/1.jpg",
      "images": "http://www.qingcheng.com/image/1.jpg,http://www.qingcheng.com/image/2.jpg",
      "status": "1",
      "weight": 130
    },
    {
      "sn": "10192010293",
      "num": 100,
      "alertNum": 20,
      "price": 600000,
      "spec": {
        "颜色": "绿",
        "机身内存": "8G"
      },
      "image": "http://www.qingcheng.com/image/1.jpg",
      "images": "http://www.qingcheng.com/image/1.jpg,http://www.qingcheng.com/image/2.jpg",
      "status": "1",
      "weight": 130
    }
  ]
}
```

## 2.3 代码生成

准备工作：为了更快的实现代码编写，我们可以采用《黑马代码生成器》来批量生成代码，这些代码就已经实现了我们之前的增删改查功能。

《黑马代码生成器》一款由传智播客教育集团JavaEE教研团队开发的基于Freemarker模板引擎的“代码生成神器”。即便是一个工程几百个表，也可以瞬间完成基础代码的构建！用户只需建立数据库表结构，运行main方法就可快速生成可以运行的一整套代码，可以极大地缩短开发周期，降低人力成本。《黑马代码生成器》的诞生主要用于迅速构建生成微服务工程的Pojo、Dao、Service、Controller各层、并且可以生成swagger API模板等。 用户通过自己开发模板也可以实现生成php、python、C# 、c++、数据库存储过程等其它编程语言的代码。

《黑马代码生成器》目前已经开源  地址：https://github.com/shenkunlin/code-template.git

将code-template导入到idea中，和changgou-parent同一级，并执行即可

使用说明,简单来说如下图所示:

![5](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/5.png)

## 2.4 代码实现

### 2.4.1 商品分类查询

#### 2.4.1.1 分析

![6](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/6.png)

在实现商品增加之前，需要先选择对应的分类，选择分类的时候，首选选择一级分类，然后根据选中的分类，将选中的分类作为查询的父ID，再查询对应的子分类集合，因此我们可以在后台编写一个方法，根据父类ID查询对应的分类集合即可。

#### 2.4.1.2 代码实现

(1)Controller层

修改`com.changgou.goods.controller.CategoryController`添加根据父ID查询所有子类集合，代码如下：

```java
/****
 * 根据节点ID查询所有子节点分类集合
 */
@GetMapping(value = "/list/{pid}")
public Result<List<Category>> findByParentId(@PathVariable(value = "pid")Integer pid){
    //调用Service实现查询
    List<Category> categories = categoryService.findByParentId(pid);
    return new Result<List<Category>>(true,StatusCode.OK,"查询成功！",categories);
}
```

(2)Service层

修改`com.changgou.goods.service.CategoryService`添加根据父类ID查询所有子节点，代码如下：

```java
/***
 * 根据分类的父ID查询子分类节点集合
 */
List<Category> findByParentId(Integer pid);
```

修改`com.changgou.goods.service.impl.CategoryServiceImpl`添加上面的实现，代码如下：

```java
/**
     * 根据分类id 获取该分类下的直接子分类列表 当ID为0的时候表示获取一级分类列表
     * @param pid
     * @return
     */
    @GetMapping("/list/{pid}")
    public Result<List<Category>> findByParentId(@PathVariable(name="pid") Integer pid) {
        List<Category> categoryList = categoryService.findByParentId(pid);
        return new Result<List<Category>>(true, StatusCode.OK, "查询成功", categoryList);
    }
```

(3)Dao层

修改`com.changgou.goods.dao.CategoryMapper`添加根据父ID查询所有子类集合，代码如下：

```java
public interface CategoryMapper extends Mapper<Category> {

    /**
     * 根据父ID 查询 分类列表
     * @param pid
     * @return
     */
    @Select(value="select * from tb_category where parent_id=#{pid}")
    List<Category> findByParentId(Integer pid);

}
```

(4)开启驼峰命名

```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: true # 开启驼峰命名 （自动将 下划线转换成大写）
```

### 2.4.2 商品分类下的品牌数据查询

#### 2.4.2.1 分析

![7](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/7.png)

用户每次选择了分类之后，可以根据用户选择的分类到`tb_category_brand`关联表中查询指定的品牌集合ID,然后根据品牌集合ID查询对应的品牌集合数据，再将品牌集合数据拿到这里来展示即可实现上述功能。

#### 2.4.2.2 代码实现

(1)Controller层

修改`com.changgou.goods.controller.BrandController`，添加根据分类ID查询对应的品牌数据代码如下：

```java
/**
     * 根据三级分类的ID 获取品牌的列表
     * @param id 三级分类的ID
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findBrandByCategory(@PathVariable(name="id")Integer id){
        List<Brand> brandList =brandService.findBrandByCategory(id);
        return new Result<List<Brand>>(true, StatusCode.OK,"根据分类查询品牌列表成功",brandList);
    }
```

(2)Service层

修改`com.changgou.goods.service.BrandService`，添加根据分类ID查询指定的品牌集合方法，代码如下：

```java
List<Brand> findBrandByCategory(Integer id);
```

修改`com.changgou.goods.service.impl.BrandServiceImpl`添加上面方法的实现，代码如下：

```java
@Override
    public List<Brand> findBrandByCategory(Integer id) {
        return brandMapper.findBrandByCategory(id);
    }
```

(3)Dao实现

修改`com.changgou.goods.dao.BrandMapper`添加根据分类ID查询对应的品牌数据，代码如下：

```java
public interface BrandMapper extends Mapper<Brand> {
    @Select(value="select tbb.* from tb_category_brand tcb,tb_brand tbb where tcb.category_id=#{id} and tbb.id=tcb.brand_id")
    List<Brand> findBrandByCategory(Integer id);
}
```

### 2.4.3 模板查询(规格参数组)

#### 2.4.3.1 分析

![8](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/8.png)

如上图，当用户选中了分类后，需要根据分类的ID查询出对应的模板数据，并将模板的名字显示在这里，模板表结构如下：

```sql
CREATE TABLE `tb_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '模板名称',
  `spec_num` int(11) DEFAULT '0' COMMENT '规格数量',
  `para_num` int(11) DEFAULT '0' COMMENT '参数数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
```

#### 2.4.3.2 代码实现

(1)Controller层

修改`com.changgou.goods.controller.SpecController`，添加根据分类ID查询模板数据：

```java
/**
     * 根据三级分类的ID 获取规格的列表
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Spec>> findByCategoryId(@PathVariable(name="id")Integer id){
        List<Spec> specList = specService.findByCategoryId(id);
        return new Result<List<Spec>>(true, StatusCode.OK,"规格查询成功",specList);
    }
```

(2)Service层

修改`com.changgou.goods.service.SpecService`接口，添加如下方法根据分类ID查询模板：

```java
public interface SpecService extends CoreService<Spec> {

    List<Spec> findByCategoryId(Integer id);
}
```

修改`com.changgou.goods.service.impl.SpecServiceImpl`添加上面方法的实现：

```java
@Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Spec> findByCategoryId(Integer id) {
        //1.根据商品分类获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(id);
        //2.获取分类对象中的模板的ID
        Integer templateId = category.getTemplateId();
        //3.根据模板的ID获取规格列表数据 select * from tb_spec where template_id=?
        Spec condition = new Spec();
        condition.setTemplateId(templateId);//where template_id=?  等号条件只能是等号
        return specMapper.select(condition);//select * from tb_spec
    }
```

### 2.4.4 规格查询

#### 2.4.4.1 分析

![9](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/9.png)

用户选择分类后，需要根据所选分类对应的模板ID查询对应的规格，规格表结构如下：

```sql
CREATE TABLE `tb_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `options` varchar(2000) DEFAULT NULL COMMENT '规格选项',
  `seq` int(11) DEFAULT NULL COMMENT '排序',
  `template_id` int(11) DEFAULT NULL COMMENT '模板ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
```

#### 2.4.4.2 代码实现

(1)Controller层

修改`com.changgou.goods.controller.SpecController`，添加根据分类ID查询模板数据：

```java
/**
     * 根据三级分类的ID 获取规格的列表
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Spec>> findByCategoryId(@PathVariable(name="id")Integer id){
        List<Spec> specList = specService.findByCategoryId(id);
        return new Result<List<Spec>>(true, StatusCode.OK,"规格查询成功",specList);
    }
```

(2)Service层

修改`com.changgou.goods.service.SpecService`接口，添加如下方法根据分类ID查询模板：

```java
public interface SpecService extends CoreService<Spec> {

    List<Spec> findByCategoryId(Integer id);
}
```

修改`com.changgou.goods.service.impl.SpecServiceImpl`添加上面方法的实现：

```java
@Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Spec> findByCategoryId(Integer id) {
        //1.根据商品分类获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(id);
        //2.获取分类对象中的模板的ID
        Integer templateId = category.getTemplateId();
        //3.根据模板的ID获取规格列表数据 select * from tb_spec where template_id=?
        Spec condition = new Spec();
        condition.setTemplateId(templateId);//where template_id=?  等号条件只能是等号
        return specMapper.select(condition);//select * from tb_spec
    }
```

### 2.4.5 参数列表查询

#### 2.4.5.1 分析

![10](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/10.png)

当用户选中分类后，需要根据分类的模板ID查询对应的参数列表，参数表结构如下：

```sql
CREATE TABLE `tb_para` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `options` varchar(2000) DEFAULT NULL COMMENT '选项',
  `seq` int(11) DEFAULT NULL COMMENT '排序',
  `template_id` int(11) DEFAULT NULL COMMENT '模板ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
```

#### 2.4.5.2 代码实现

(1)Controller层

修改`com.changgou.goods.controller.ParaController`，添加根据分类ID查询参数列表，代码如下：

```java
/**
     * 根据三级分类的ID 获取参数列表
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Para>> findByCategoryId(@PathVariable(name="id")Integer id){
        List<Para> paraList = paraService.findByCategoryId(id);
        return new Result<List<Para>>(true, StatusCode.OK,"参数查询成功",paraList);
    }
```

(2)Service层

修改`com.changgou.goods.service.ParaService`添加根据分类ID查询参数列表，代码如下：

```java
public interface ParaService extends CoreService<Para> {

    List<Para> findByCategoryId(Integer id);
}
```

修改`com.changgou.goods.service.impl.ParaServiceImpl`添加上面方法的实现，代码如下：

```java
@Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Para> findByCategoryId(Integer id) {
        //1.根据商品分类获取分类对象
        Category category = categoryMapper.selectByPrimaryKey(id);
        //2.获取分类对象中的模板的ID
        Integer templateId = category.getTemplateId();
        //3.根据模板的ID获取参数列表数据 select * from tb_para where template_id=?
        Para condition = new Para();
        condition.setTemplateId(templateId);//where template_id=?  等号条件只能是等号
        return paraMapper.select(condition);//select * from tb_para
    }
```

### 2.4.6 添加数据(SPU+SKU保存)

#### 2.4.6.1 分析

保存商品数据的时候，需要保存Spu和Sku，一个Spu对应多个Sku，我们可以先构建一个Goods对象，将`Spu`和`List<Sku>`组合到一起,前端将2者数据提交过来，再实现添加操作。

#### 2.4.6.2 雪花算法

是由64位二进制的数据组成(时间戳+工作机器ID+数据中心ID+序列)共同组成的数字

一会儿会用到ID生成，我们可以使用IdWorker，在启动类GoodsApplication中添加如下代码,用于创建IdWorker，并将IdWorker交给Spring容器，代码如下：

```java
/***
 * IdWorker
 * @return
 */
@Bean
public IdWorker idWorker(){
    return new IdWorker(0,0);
}
```

#### 2.4.6.3 代码实现

(1)Pojo改造

修改changgou-service-goods-api工程创建组合实体类，创建com.changgou.goods.pojo.Goods,代码如下：

```java
public class Goods implements Serializable {
    //SPU
    private Spu spu;
    //SKU集合
    private List<Sku> skuList;

    //..get..set..toString
}
```

(2) 业务层

修改com.changgou.goods.service.SpuService接口，添加保存Goods方法，代码如下：

```java
/**
 * 保存商品
 * @param goods
 */
void save(Goods goods);
```

修改com.changgou.goods.service.impl.SpuServiceImpl类，添加保存Goods的方法实现，代码如下：

```java

/****
 * @Author:admin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl extends CoreServiceImpl<Spu> implements SpuService {

    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper) {
        super(spuMapper, Spu.class);
        this.spuMapper = spuMapper;
    }

    //判断 如果页面传递过来了spu的ID 说明是要修改
    // 如果页面没有传递来spu的ID说明是要添加
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Goods goods) {
        //1.获取页面传递过来的spu的数据  添加到spu表中
        Spu spu = goods.getSpu();

        //1.1 先生成主键
        long id = idWorker.nextId();
        spu.setId(id);

        spuMapper.insertSelective(spu);
        //2.获取页面传递过来的sku的列表数据 添加到sku表中
        List<Sku> skuList = goods.getSkuList();

        //批量添加
        for (Sku sku : skuList) {
            //2.1 生成sku的主键 雪花算法来生成
            long skuId = idWorker.nextId();
            sku.setId(skuId);
            //2.2 设置skuname 要求：将spu的name + 空格 + 规格选项值 例如：商品规格：颜色 内存容量---》华为mate40 黑色 256G
            //{"电视音响效果":"立体声","电视屏幕尺寸":"20英寸","尺码":"165"}
            //思路：1.获取页面传递过来的规格的数据是一个JSON -->2.将JSON转成MAP--->3 循环遍历 获取map中的 value 4.通过空格拼接 得到sku的名称
            String spec = sku.getSpec();
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            String name = spu.getName();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //黑色
                String value = entry.getValue();
                // 商品名 黑色 256G
                name += " " + value;
            }
            sku.setName(name);
            //2.3设置创建时间 和更新时间
            sku.setCreateTime(new Date());
            sku.setUpdateTime(sku.getCreateTime());
            //2.4设置spu的ID
            sku.setSpuId(spu.getId());
            //2.5 设置分类的ID 和名称（根据spu中的categoy3ID 获取分类表中的分类数据（id和name） -->再设置给sku的categoy_id 和category_name字段）
            Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
            if (category != null) {
                sku.setCategoryId(category.getId());
                sku.setCategoryName(category.getName());
            }
            //2.6设置品牌名字 根据spu的brand_id 从brand表中获取品牌的名称 ---》再设置sku的brand_name
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            sku.setBrandName(brand.getName());

            skuMapper.insertSelective(sku);
        }


    }
}
```

(3)控制层

修改com.changgou.goods.controller.SpuController，增加保存Goods方法，代码如下：

```java
//保存数据到 sku 和spu表中
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods){
        spuService.save(goods);
        return new Result<List<Para>>(true, StatusCode.OK,"添加商品成功");
    }
```

postman测试数据

```json
{
  "skuList": [
    {
      "alertNum": 10,
      "brandName": "华为",
      "categoryId": 64,
      "commentNum": 0,
      "image": "http://www.baidu.com",
      "images": "",
      "name": "华为P30手机",
      "num": 5,
      "price": 1000,
      "saleNum": 0,
      "sn": "No1001",
      "spec": "{\"颜色\":\"红\",\"机身内存\":\"64G\"}",
      "weight": 0
    },
    {
      "alertNum": 10,
      "brandName": "华为",
      "categoryId": 64,
      "commentNum": 0,
      "image": "http://www.baidu.com",
      "images": "",
      "name": "华为P30手机",
      "num": 5,
      "price": 1000,
      "saleNum": 0,
      "sn": "No1001",
      "spec": "{\"颜色\":\"绿\",\"机身内存\":\"64G\"}",
      "weight": 0
    },
     {
      "alertNum": 10,
      "brandName": "华为",
      "categoryId": 64,
      "commentNum": 0,
      "image": "http://www.baidu.com",
      "images": "",
      "name": "华为P30手机",
      "num": 5,
      "price": 1000,
      "saleNum": 0,
      "sn": "No1001",
      "spec": "{\"颜色\":\"绿\",\"机身内存\":\"8G\"}",
      "weight": 0
    },
     {
      "alertNum": 10,
      "brandName": "华为",
      "categoryId": 64,
      "commentNum": 0,
      "image": "http://www.baidu.com",
      "images": "",
      "name": "华为P30手机",
      "num": 5,
      "price": 1000,
      "saleNum": 0,
      "sn": "No1001",
      "spec": "{\"颜色\":\"红\",\"机身内存\":\"8G\"}",
      "weight": 0
    }
  ],
  "spu": {
    "brandId": 8557,
    "caption": "104期手机大促销",
    "category1Id": 1,
    "category2Id": 59,
    "category3Id": 64,
    "commentNum": 0,
    "freightId": 0,
    "images": "http://www.qingcheng.com/image/1.jpg,http://www.qingcheng.com/image/2.jpg",
    "introduction": "华为产品世界最强",
    "isEnableSpec": "1",
    "isMarketable": "1",
    "name": "104期特牛逼的手机",
    "specItems": "{\"颜色\":[\"红\",\"绿\"],\"机身内存\":[\"64G\",\"8G\"]}",
    "paraItems": "{\"赠品\":\"充电器\",\"出厂年份\":\"2019\"}",
    "saleNum": 0,
    "saleService": "一年包换",
    "sn": "No10001",
    "status": "1",
    "templateId": 42
  }
}
```

### 2.4.7 修改数据

因为实际业务中是点击具体条目进行编辑(修改)，所以实际上是根据ID查询商品，然后进行修改

#### 2.4.7.1 需求分析

```
1 回显数据(根据id先查询并显示原先的数据)
	请求： /spu/goods/{id}    GET
	参数： spu的ID 
	返回值：Result<Goods>
2 修改数据    判断是否更新和添加（通过spu的ID是否有值来判断）
	请求： /spu/save  POST
	参数：goods
	返回值：result 
```

需求：根据id 查询SPU和SKU列表 ，显示效果如下：

```json
{
    "spu": {
		"brandId": 0,
		"caption": "111",
		"category1Id": 558,
		"category2Id": 559,
		"category3Id": 560,
		"commentNum": null,
		"freightId": null,
		"id": 149187842867993,
		"image": null,
		"images": null,
		"introduction": null,
		"isDelete": null,
		"isEnableSpec": "0",
		"isMarketable": "1",
		"name": "黑马智能手机",
		"paraItems": null,
		"saleNum": null,
		"saleService": null,
		"sn": null,
		"specItems": null,
		"status": null,
		"templateId": 42
	},
	"skuList": [{
		"alertNum": null,
		"brandName": "金立（Gionee）",
		"categoryId": 560,
		"categoryName": "手机",
		"commentNum": null,
		"createTime": "2018-11-06 10:17:08",
		"id": 1369324,
		"image": null,
		"images": "blob:http://localhost:8080/ec04d1a5-d865-4e7f-a313-2e9a76cfb3f8",
		"name": "黑马智能手机",
		"num": 100,
		"price": 900000,
		"saleNum": null,
		"sn": "",
		"spec": null,
		"spuId": 149187842867993,
		"status": "1",
		"updateTime": "2018-11-06 10:17:08",
		"weight": null
	},{
		"alertNum": null,
		"brandName": "金立（Gionee）",
		"categoryId": 560,
		"categoryName": "手机",
		"commentNum": null,
		"createTime": "2018-11-06 10:17:08",
		"id": 1369325,
		"image": null,
		"images": "blob:http://localhost:8080/ec04d1a5-d865-4e7f-a313-2e9a76cfb3f8",
		"name": "黑马智能手机",
		"num": 100,
		"price": 900000,
		"saleNum": null,
		"sn": "",
		"spec": null,
		"spuId": 149187842867993,
		"status": "1",
		"updateTime": "2018-11-06 10:17:08",
		"weight": null
	  }
   ]
}
```

#### 2.4.7.2 代码实现(回显)

(1)业务层

修改changgou-service-goods工程,修改com.changgou.goods.service.SpuService接口,添加根据ID查找方法findGoodsById代码如下：

```java
/***
 * 根据SPU的ID查找SPU以及对应的SKU集合
 * @param spuId
 */
Goods findGoodsById(Long spuId);
```

修改qingcheng-service-goods工程，修改com.changgou.goods.service.impl.SpuServiceImpl类，添加根据ID查找findGoodsById方法，代码如下：

```java
/***
 * 根据SpuID查询goods信息
 * @param spuId
 * @return
 */
@Override
public Goods findGoodsById(Long spuId) {
    //查询Spu
    Spu spu = spuMapper.selectByPrimaryKey(spuId);

    //查询List<Sku>
    Sku sku = new Sku();
    sku.setSpuId(spuId);
    List<Sku> skus = skuMapper.select(sku);
    //封装Goods
    Goods goods = new Goods();
    goods.setSkus(skus);
    goods.setSpu(spu);
    return goods;
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，修改findById方法，代码如下：

```java
/***
 * 根据ID查询Goods
 * @param id
 * @return
 */
@GetMapping("/goods/{id}")
public Result<Goods> findGoodsById(@PathVariable Long id){
    //根据ID查询Goods(SPU+SKU)信息
    Goods goods = spuService.findGoodsById(id);
    return new Result<Goods>(true,StatusCode.OK,"查询成功",goods);
}
```

测试：`http://localhost:18081/spu/goods/spuId

#### 2.4.7.3 保存修改并提交(类似添加)

<font color='red'>合并添加和修改的逻辑，spu的规格变化时，会导致sku的数目变化。因此，先删除原来的sku列表，再添加sku列表，否则sku增加或者减少，单独判断逻辑很麻烦</font>

修改changgou-service-goods的SpuServiceImpl的saveGoods方法，修改添加SPU部分代码：

```java
//判断 如果页面传递过来了spu的ID 说明是要修改
    // 如果页面没有传递来spu的ID说明是要添加
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Goods goods) {
        //1.获取页面传递过来的spu的数据  添加到spu表中
        Spu spu = goods.getSpu();

        //判断是否spu的ID 有值 如果有值 就是更新 否则就是添加
        if (spu.getId() != null) {
            //更新spu
            spuMapper.updateByPrimaryKeySelective(spu);
            //先删除原来的sku的列表 再 添加sku表
            //delete from tb_sku where spu_id=?
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        } else {
            //1.1 先生成主键
            long id = idWorker.nextId();
            spu.setId(id);
            spuMapper.insertSelective(spu);
        }
        saveSku(goods);
    }

    private void saveSku(Goods goods) {
        Spu spu = goods.getSpu();
        //2.获取页面传递过来的sku的列表数据 添加到sku表中
        List<Sku> skuList = goods.getSkuList();

        //批量添加
        for (Sku sku : skuList) {
            //2.1 生成sku的主键 雪花算法来生成
            long skuId = idWorker.nextId();
            sku.setId(skuId);
            //2.2 设置skuname 要求：将spu的name + 空格 + 规格选项值 例如：商品规格：颜色 内存容量---》华为mate40 黑色 256G
            //{"电视音响效果":"立体声","电视屏幕尺寸":"20英寸","尺码":"165"}
            //思路：1.获取页面传递过来的规格的数据是一个JSON -->2.将JSON转成MAP--->3 循环遍历 获取map中的 value 4.通过空格拼接 得到sku的名称
            String spec = sku.getSpec();
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            String name = spu.getName();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                //黑色
                String value = entry.getValue();
                // 商品名 黑色 256G
                name += " " + value;
            }
            sku.setName(name);
            //2.3设置创建时间 和更新时间
            sku.setCreateTime(new Date());
            sku.setUpdateTime(sku.getCreateTime());
            //2.4设置spu的ID
            sku.setSpuId(spu.getId());
            //2.5 设置分类的ID 和名称（根据spu中的categoy3ID 获取分类表中的分类数据（id和name） -->再设置给sku的categoy_id 和category_name字段）
            Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
            if (category != null) {
                sku.setCategoryId(category.getId());
                sku.setCategoryName(category.getName());
            }
            //2.6设置品牌名字 根据spu的brand_id 从brand表中获取品牌的名称 ---》再设置sku的brand_name
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            sku.setBrandName(brand.getName());

            skuMapper.insertSelective(sku);
        }
    }
```

### 2.4.8 修改SKU库存

（学员实现）

# 3. 商品审核与上下架

## 3.1 需求分析

商品新增后，审核状态为0（未审核），默认为下架状态。

审核商品，需要校验是否是被删除的商品，如果未删除则修改审核状态为1

下架商品，需要校验是否是被删除的商品，如果未删除则修改上架状态为0

上架商品，需要校验是否被删除的商品,如果未被删除,则需要审核通过的商品，才能上架

## 3.2 实现思路

（1）按照ID查询SPU信息

（2）判断修改审核、上架和下架状态

（3）保存SPU

## 3.3 代码实现

### 3.3.1 商品审核

实现审核通过，自动上架。

(1)业务层

修改修改changgou-service-goods工程的com.changgou.goods.service.SpuService接口，添加审核方法，代码如下：

```java
/***
 * 商品审核
 * @param spuId
 */
void audit(Long spuId);
```

修改changgou-service-goods工程的com.changgou.goods.service.impl.SpuServiceImpl类，添加audit方法，代码如下：

```java
/***
 * 商品审核
 * @param spuId
 */
@Override
public void audit(Long spuId) {
    //查询商品
    Spu spu = spuMapper.selectByPrimaryKey(spuId);
    //判断商品是否已经删除
    if(spu.getIsDelete().equalsIgnoreCase("1")){
        throw new RuntimeException("该商品已经删除！");
    }
    //实现审核
    spu.setStatus("1"); //审核通过    
    spuMapper.updateByPrimaryKeySelective(spu);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，新增audit方法，代码如下：

```java
/**
 * 审核
 * @param id
 * @return
 */
@PutMapping("/audit/{id}")
public Result audit(@PathVariable Long id){
    spuService.audit(id);
    return new Result(true,StatusCode.OK,"审核成功");
}
```

### 3.3.2 下架商品

(1)业务层

修改com.changgou.goods.service.SpuService接口，添加pull方法，用于商品下架，代码如下：

```java
/***
 * 商品下架
 * @param spuId
 */
void pull(Long spuId);
```

修改com.changgou.goods.service.impl.SpuServiceImpl，添加如下方法：

```java
/**
 * 商品下架
 * @param spuId
 */
@Override
public void pull(Long spuId) {
    Spu spu = spuMapper.selectByPrimaryKey(spuId);
    if(spu.getIsDelete().equals("1")){
        throw new RuntimeException("此商品已删除！");
    }
    spu.setIsMarketable("0");//下架状态
    spuMapper.updateByPrimaryKeySelective(spu);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，添加pull方法，代码如下：

```java
/**
 * 下架
 * @param id
 * @return
 */
@PutMapping("/pull/{id}")
public Result pull(@PathVariable Long id){
    spuService.pull(id);
    return new Result(true,StatusCode.OK,"下架成功");
}
```

### 3.3.3 上架商品 

(1)业务层

修改com.changgou.goods.service.SpuService，添加put方法，代码如下：

```java
/***
 * 商品上架
 * @param spuId
 */
void put(Long spuId);
```

修改com.changgou.goods.service.impl.SpuServiceImpl，添加put方法实现，代码如下：

```java
/***
 * 商品上架
 * @param spuId
 */
@Override
public void put(Long spuId) {
    Spu spu = spuMapper.selectByPrimaryKey(spuId);
    //检查是否删除的商品
    if(spu.getIsDelete().equals("1")){
        throw new RuntimeException("此商品已删除！");
    }   
    //上架状态
    spu.setIsMarketable("1");
    spuMapper.updateByPrimaryKeySelective(spu);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，添加put方法代码如下：

```java
/**
 * 商品上架
 * @param id
 * @return
 */
@PutMapping("/put/{id}")
public Result put(@PathVariable Long id){
    spuService.put(id);
    return new Result(true,StatusCode.OK,"上架成功");
}
```

### 3.3.4 批量上架 

前端传递一组商品ID，后端进行批量上下架处理

(1)业务层

修改com.changgou.goods.service.SpuService接口，代码如下：

```java
int putMany(Long[] ids);
```

修改com.changgou.goods.service.impl.SpuServiceImpl，添加批量上架方法实现，代码如下：

```java
/***
 * 批量上架
 * @param ids:需要上架的商品ID集合
 * @return
 */
@Override
public int putMany(Long[] ids) {
    Spu spu=new Spu();
    spu.setIsMarketable("1");//上架
    //批量修改
    Example example=new Example(Spu.class);
    Example.Criteria criteria = example.createCriteria();
    criteria.andIn("id", Arrays.asList(ids));//id
    //下架
    criteria.andEqualTo("isMarketable","0");
    //审核通过的
    criteria.andEqualTo("status","1");
    //非删除的
    criteria.andEqualTo("isDelete","0");
    return spuMapper.updateByExampleSelective(spu, example);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，添加批量上架方法，代码如下：

```java
/**
 *  批量上架
 * @param ids
 * @return
 */
@PutMapping("/put/many")
public Result putMany(@RequestBody Long[] ids){
    int count = spuService.putMany(ids);
    return new Result(true,StatusCode.OK,"上架"+count+"个商品");
}
```

使用Postman测试：

![11](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/11.png)

### 3.3.5 批量下架

学员实现

# 4. 删除与还原商品 

## 4.1 需求分析 

请看管理后台的静态原型

商品列表中的删除商品功能，并非真正的删除，而是将删除标记的字段设置为1，

在回收站中有恢复商品的功能，将删除标记的字段设置为0

在回收站中有删除商品的功能，是真正的物理删除。

## 4.2 实现思路 

逻辑删除商品，修改spu表is_delete字段为1

商品回收站显示spu表is_delete字段为1的记录

回收商品，修改spu表is_delete字段为0

## 4.3 代码实现 

### 4.3.1 逻辑删除商品 

(1)业务层

修改com.changgou.goods.service.SpuService接口，增加logicDelete方法，代码如下：

```java
/***
 * 逻辑删除
 * @param spuId
 */
void logicDelete(Long spuId);
```

修改com.changgou.goods.service.impl.SpuServiceImpl，添加logicDelete方法实现，代码如下：

```java
/***
 * 逻辑删除
 * @param spuId
 */
@Override
@Transactional
public void logicDelete(Long spuId) {
    Spu spu = spuMapper.selectByPrimaryKey(spuId);
    //检查是否下架的商品
    if(!spu.getIsMarketable().equals("0")){
        throw new RuntimeException("必须先下架再删除！");
    }
    //删除
    spu.setIsDelete("1");
    //未审核
    spu.setStatus("0");
    spuMapper.updateByPrimaryKeySelective(spu);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，添加logicDelete方法，如下：

```java
/**
 * 逻辑删除
 * @param id
 * @return
 */
@DeleteMapping("/logic/delete/{id}")
public Result logicDelete(@PathVariable Long id){
    spuService.logicDelete(id);
    return new Result(true,StatusCode.OK,"逻辑删除成功！");
}
```

### 4.3.2 还原被删除的商品 

(1)业务层

修改com.changgou.goods.service.SpuService接口，添加restore方法代码如下：

```java
/***
 * 还原被删除商品
 * @param spuId
 */
void restore(Long spuId);
```

修改com.changgou.goods.service.impl.SpuServiceImpl类，添加restore方法，代码如下：

```java
/**
 * 恢复数据
 * @param spuId
 */
@Override
public void restore(Long spuId) {
    Spu spu = spuMapper.selectByPrimaryKey(spuId);
    //检查是否删除的商品
    if(!spu.getIsDelete().equals("1")){
        throw new RuntimeException("此商品未删除！");
    }
    //未删除
    spu.setIsDelete("0");
    //未审核
    spu.setStatus("0");
    spuMapper.updateByPrimaryKeySelective(spu);
}
```

(2)控制层

修改com.changgou.goods.controller.SpuController，添加restore方法，代码如下：

```java
/**
 * 恢复数据
 * @param id
 * @return
 */
@PutMapping("/restore/{id}")
public Result restore(@PathVariable Long id){
    spuService.restore(id);
    return new Result(true,StatusCode.OK,"数据恢复成功！");
}
```

### 4.3.3 物理删除商品  

修改com.changgou.goods.service.impl.SpuServiceImpl的delete方法,代码如下：

```java
/**
 * 删除
 * @param id
 */
@Override
public void delete(Long id){
    Spu spu = spuMapper.selectByPrimaryKey(id);
    //检查是否被逻辑删除  ,必须先逻辑删除后才能物理删除
    if(!spu.getIsDelete().equals("1")){
        throw new RuntimeException("此商品不能删除！");
    }
    spuMapper.deleteByPrimaryKey(id);
}
```

# 5. 商品列表

## 5.1 需求分析

如图所示 展示商品的列表。并实现分页。

![12](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/12.png)

思路：

```properties
根据查询的条件 分页查询 并返回分页结果即可。
分页查询 采用pagehelper 条件查询  通过map进行封装传递给后台即可。
```

## 5.2 代码实现

在代码生成器生成的代码中已经包含了该实现，这里就省略了。

控制层（SpuController）:

```java
/***
 * Spu分页条件搜索实现
 * @param spu
 * @param page
 * @param size
 * @return
 */
@PostMapping(value = "/search/{page}/{size}" )
public Result<PageInfo> findPage(@RequestBody(required = false) Spu spu, @PathVariable  int page, @PathVariable  int size){
    //执行搜索
    PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
    return new Result(true,StatusCode.OK,"查询成功",pageInfo);
}
```

其他每层代码，代码生成器已经生成，这里就不再列出来了。

# 6. 总结

![13](https://raw.githubusercontent.com/Novak666/Learning-working-skill/main/畅购/2021.10.10/pics/13.png)