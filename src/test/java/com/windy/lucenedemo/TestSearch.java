package com.windy.lucenedemo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.nio.file.Paths;

@SpringBootTest
public class TestSearch {
    @Test
    void testSearch() throws Exception {

        //1. 创建分词器(对搜索的关键词进行分词使用)
        //注意: 分词器要和创建索引的时候使用的分词器一模一样
        Analyzer analyzer = new StandardAnalyzer();
        //Analyzer cjkanalyzer = new CJKAnalyzer();

        //2. 创建查询对象,
        //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
        //第二个参数: 使用的分词器
        QueryParser queryParser = new QueryParser("name", analyzer);

        //3. 设置搜索关键词
        Query query = queryParser.parse("华为手机");

        //4. 创建Directory目录对象, 指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
        //5. 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);
        //6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //7. 搜索, 并返回结果
        //第二个参数: 是返回多少条数据用于展示, 分页使用
        TopDocs topDocs = indexSearcher.search(query, 10);

        //获取查询到的结果集的总数, 打印
        System.out.println("=======count=======" + topDocs.totalHits);

        //8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //9. 遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                int docID = scoreDoc.doc;
                //通过文档id, 读取文档
                Document doc = indexSearcher.doc(docID);
                System.out.println("==================================================");
                //通过域名, 从文档中获取域值
                System.out.println("===id==" + doc.get("id"));
                System.out.println("===name==" + doc.get("name"));
                System.out.println("===price==" + doc.get("price"));
                System.out.println("===num==" + doc.get("num"));
                System.out.println("===image==" + doc.get("image"));
                System.out.println("===brandName==" + doc.get("brandName"));
                System.out.println("===categoryName==" + doc.get("categoryName"));
                System.out.println("===spec==" + doc.get("spec"));
                System.out.println("===saleNum==" + doc.get("saleNum"));


            }
        }
        //10. 关闭流
        indexReader.close();

    }


    // 文本搜索:使用IK分词  查询 brandName包含华为手机的记录
    @Test
    void testSearchIK() throws Exception {

        //1. 创建分词器(对搜索的关键词进行分词使用)
        //注意: 分词器要和创建索引的时候使用的分词器一模一样
        Analyzer analyzer = new IKAnalyzer();


        //2. 创建查询对象,
        //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
        //第二个参数: 使用的分词器
        QueryParser queryParser = new QueryParser("brandName", analyzer);

        //3. 设置搜索关键词
        //华 OR  为   手   机
        Query query = queryParser.parse("name:华为手机");

        //4. 创建Directory目录对象, 指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
        //5. 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);
        //6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //7. 搜索, 并返回结果
        //第二个参数: 是返回多少条数据用于展示, 分页使用
        TopDocs topDocs = indexSearcher.search(query, 50);

        //获取查询到的结果集的总数, 打印
        System.out.println("=======查询到的总记录数=======" + topDocs.totalHits);

        //8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //9. 遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                int docID = scoreDoc.doc;
                //通过文档id, 读取文档
                Document doc = indexSearcher.doc(docID);
                System.out.println("==================================================");
                //通过域名, 从文档中获取域值
                System.out.println("===id==" + doc.get("id"));
                System.out.println("===name==" + doc.get("name"));
                System.out.println("===price==" + doc.get("price"));
                System.out.println("===num==" + doc.get("num"));
                System.out.println("===image==" + doc.get("image"));
                System.out.println("===brandName==" + doc.get("brandName"));
                System.out.println("===categoryName==" + doc.get("categoryName"));
                System.out.println("===spec==" + doc.get("spec"));
                System.out.println("===saleNum==" + doc.get("saleNum"));


            }
        }
        //10. 关闭流
        indexReader.close();

    }


    // 范围搜索:使用IK分词  查询 price在某个范围内的记录
    @Test
    void testSearchIKRange() throws Exception {

        //1. 创建分词器(对搜索的关键词进行分词使用)
        //注意: 分词器要和创建索引的时候使用的分词器一模一样
        Analyzer analyzer = new IKAnalyzer();


        //2. 创建查询对象,
        //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
        //第二个参数: 使用的分词器
        QueryParser queryParser = new QueryParser("name",analyzer);

        //3. 设置搜索关键词


//        注意这里的query的point一定要和创建索引时指定的point相同,否则查询不到数据
   Query query = IntPoint.newRangeQuery("price", 0, 99999);


        //4. 创建Directory目录对象, 指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
        //5. 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);
        //6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //7. 搜索, 并返回结果
        //第二个参数: 是返回多少条数据用于展示, 分页使用
        TopDocs topDocs = indexSearcher.search(query, 50);

        //获取查询到的结果集的总数, 打印
        System.out.println("=======查询到的总记录数=======" + topDocs.totalHits);

        //8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //9. 遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                int docID = scoreDoc.doc;
                //通过文档id, 读取文档
                Document doc = indexSearcher.doc(docID);
                System.out.println("==================================================");
                //通过域名, 从文档中获取域值
                System.out.println("===id==" + doc.get("id"));
                System.out.println("===name==" + doc.get("name"));
                System.out.println("===price==" + doc.get("price"));
                System.out.println("===num==" + doc.get("num"));
                System.out.println("===image==" + doc.get("image"));
                System.out.println("===brandName==" + doc.get("brandName"));
                System.out.println("===categoryName==" + doc.get("categoryName"));
                System.out.println("===spec==" + doc.get("spec"));
                System.out.println("===saleNum==" + doc.get("saleNum"));


            }
        }
        //10. 关闭流
        indexReader.close();

    }

//组合查询 查询名字包含华为手机并且价格在100-1000元的商品


    @Test
    void testCombinationSearch() throws Exception {

        //1. 创建分词器(对搜索的关键词进行分词使用)
        //注意: 分词器要和创建索引的时候使用的分词器一模一样
        Analyzer analyzer = new IKAnalyzer();


        //2. 创建查询对象,
        //第一个参数: 默认查询域, 如果查询的关键字中带搜索的域名, 则从指定域中查询, 如果不带域名则从, 默认搜索域中查询
        //第二个参数: 使用的分词器
        QueryParser queryParser = new QueryParser("brandName", analyzer);

        //3. 设置搜索关键词
        //华 OR  为   手   机
        Query query1 = queryParser.parse("name:华为手机");
        Query query2 = FloatPoint.newRangeQuery("price", 100, 1000);
//        创建boolean查询对象
        BooleanQuery.Builder query = new BooleanQuery.Builder();
//        添加子查询并指定子查询条件
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.MUST);
        

        //4. 创建Directory目录对象, 指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\IDEAWorkSpace\\dic"));
        //5. 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);
        //6. 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //7. 搜索, 并返回结果
        //第二个参数: 是返回多少条数据用于展示, 分页使用
        TopDocs topDocs = indexSearcher.search(query.build(), 50);

        //获取查询到的结果集的总数, 打印
        System.out.println("=======查询到的总记录数=======" + topDocs.totalHits);

        //8. 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //9. 遍历结果集
        if (scoreDocs != null) {
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取查询到的文档唯一标识, 文档id, 这个id是lucene在创建文档的时候自动分配的
                int docID = scoreDoc.doc;
                //通过文档id, 读取文档
                Document doc = indexSearcher.doc(docID);
                System.out.println("==================================================");
                //通过域名, 从文档中获取域值
                System.out.println("===id==" + doc.get("id"));
                System.out.println("===name==" + doc.get("name"));
                System.out.println("===price==" + doc.get("price"));
                System.out.println("===num==" + doc.get("num"));
                System.out.println("===image==" + doc.get("image"));
                System.out.println("===brandName==" + doc.get("brandName"));
                System.out.println("===categoryName==" + doc.get("categoryName"));
                System.out.println("===spec==" + doc.get("spec"));
                System.out.println("===saleNum==" + doc.get("saleNum"));


            }
        }
        //10. 关闭流
        indexReader.close();

    }


}
