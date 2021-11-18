package com.windy.lucenedemo;

import com.windy.lucenedemo.dao.SkuDAO;
import com.windy.lucenedemo.dao.impl.SkuDAOImpl;
import com.windy.lucenedemo.pojo.Sku;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestIndexManager {
    @Test
    void testIndexManager() throws IOException {
//        1.采集数据
        SkuDAO skuDAO = new SkuDAOImpl();
        List<Sku> skuList = skuDAO.querySkuList();
//        Document引入的是org.apache.lucene.document.Document
        List<Document> documentList = new ArrayList<>();

        for (Sku sku : skuList) {
            //        2.创建文档对象
            Document document = new Document();
//            创建域对象 并放入文档对象中 不是所有字段都需要创建域对象   用户经常查询的才创建
//            此处先演示文本域
//            商品id 不分词 索引 存储    StringField不分词  会将整个串存储在索引中
            document.add(new StringField("id", sku.getId(), Field.Store.YES));
//            商品名称  分词 索引 存储    TextField会分词 将分词的结果分别存入索引中
            document.add(new TextField("name", sku.getName(), Field.Store.YES));
//            商品价格  分词 索引 存储    IntPoint 会分词 创建索引 但是不会存储 因此需要额外添加对该字段的存储 不排序
            document.add(new IntPoint("price", sku.getPrice()));
//            添加对 price 的存储
            document.add(new StoredField("price", sku.getPrice()));
//            商品数量  不分词 不索引 存储
            document.add(new StoredField("num", sku.getNum()));
//            图片地址  不分词 不索引 存储
            document.add(new StoredField("image", sku.getImage()));
//            分类名称   不分词 索引 存储
            document.add(new StringField("categoryName", sku.getCategoryName(), Field.Store.YES));
//            品牌名称  不分词 索引 存储
            document.add(new StringField("brandName", sku.getBrandName(), Field.Store.YES));
//           分类名称  不分词 索引 存储
            document.add(new StringField("spec", sku.getSpec(), Field.Store.YES));
//            销售数量  不分词 索引 存储
            document.add(new StringField("saleNum", String.valueOf(sku.getSaleNum()), Field.Store.YES));
//            将文档对象添加到集合中
            documentList.add(document);
        }
//        3.创建分词器  StandardAnalyzer 标准分词器 对英文分词较好 对中文只是拆分单字
//        Analyzer analyzer = new IKAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
//        4.创建Directory目录对象 目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
//        5.创建IndexWriterConfig对象  该对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        6.创建IndexWriter输出流对象  指定输出位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
//        7.写入文档到索引库
        for (Document doc :
                documentList) {
            indexWriter.addDocument(doc);

        }
//        8.释放资源
        indexWriter.close();
    }

    @Test
    void testIndexUpdate() throws IOException {

        Document document = new Document();
//            创建域对象 并放入文档对象中 不是所有字段都需要创建域对象   用户经常查询的才创建

        document.add(new StringField("id", "12345687", Field.Store.YES));

        document.add(new TextField("name", "xxxxxxx", Field.Store.YES));

        document.add(new FloatPoint("price", 1234));
//            添加对 price 的存储
//            document.add(new NumericDocValuesField("price",sku.getPrice()));

        document.add(new StoredField("num", 11));

        document.add(new StoredField("image", "xxxx.org"));

        document.add(new StringField("categoryName", "手机", Field.Store.YES));

        document.add(new StringField("brandName", "华为", Field.Store.YES));

        document.add(new StringField("spec", "5G", Field.Store.YES));

        document.add(new StringField("saleNum", "55", Field.Store.YES));

//        3.创建分词器  StandardAnalyzer 标准分词器 对英文分词较好 对中文只是拆分单字
        Analyzer analyzer = new StandardAnalyzer();
//        4.创建Directory目录对象 目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
//        5.创建IndexWriterConfig对象  该对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        6.创建IndexWriter输出流对象  指定输出位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
//        7.修改文档 第一个参数:修改条件 term 第二个参数：修改的内容
//        注意 默认情况下  被修改的内容会首先被删除 然后再在文档的末尾 添加上修改的数据
        indexWriter.updateDocument(new Term("id", "100000003145"), document);
//        8.释放资源
        indexWriter.close();


    }


    @Test
    void testIndexDelete() throws IOException {
//        3.创建分词器  StandardAnalyzer 标准分词器 对英文分词较好 对中文只是拆分单字
        Analyzer analyzer = new StandardAnalyzer();
//        4.创建Directory目录对象 目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
//        5.创建IndexWriterConfig对象  该对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        6.创建IndexWriter输出流对象  指定输出位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
//        7.删除文档 传入term 条件
        indexWriter.deleteDocuments(new Term("price", "100000003145"));
//        8.释放资源
        indexWriter.close();


    }

    @Test
    void testIK() throws IOException {
        //3.创建分词器  StandardAnalyzer 标准分词器 对英文分词较好 对中文只是拆分单字
        Analyzer analyzer = new IKAnalyzer();
//        4.创建Directory目录对象 目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
//        5.创建IndexWriterConfig对象  该对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        6.创建IndexWriter输出流对象  指定输出位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
//        7.写入文档到索引库
        Document document = new Document();
//
        document.add(new TextField("name", "vivo X23 8GB+128GB 幻夜蓝,水滴屏全面屏,游戏手机.移 动联通电信全网通4G手机", Field.Store.YES));
        indexWriter.addDocument(document);

//        8.释放资源
        indexWriter.close();


    }

    @Test
    void testDelete() throws IOException {
        //3.创建分词器  StandardAnalyzer 标准分词器 对英文分词较好 对中文只是拆分单字
        Analyzer analyzer = new IKAnalyzer();
//        4.创建Directory目录对象 目录对象表示索引库的位置
        Directory directory = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
//        5.创建IndexWriterConfig对象  该对象中指定切分词使用的分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//        6.创建IndexWriter输出流对象  指定输出位置和使用的config初始化对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
//        7.写入文档到索引库
        Document document = new Document();
//
        document.add(new TextField("name", "vivo X23 8GB+128GB 幻夜蓝,水滴屏全面屏,游戏手机.移 动联通电信全网通4G手机", Field.Store.YES));
        indexWriter.addDocument(document);

//        8.释放资源
        indexWriter.close();


    }


}
