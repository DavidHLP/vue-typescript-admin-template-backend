package com.david.hlp.SpringBootWork.blog.service;

import com.david.hlp.SpringBootWork.blog.entity.ArticleTable;
import com.david.hlp.SpringBootWork.blog.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务层：DraggableTableService。
 *
 * 提供文章表的业务逻辑处理，包括增删改查和复杂查询。
 */
@Service // 标记为 Spring 服务组件
@RequiredArgsConstructor // 自动生成包含所有必需依赖项的构造函数
public class ArticleService {

    private final ArticleMapper articleMapper; // MyBatis Mapper

    /**
     * 更新文章。
     *
     * @param id             文章的唯一标识。
     * @param articleDetails 更新的文章内容。
     * @return 更新后的文章对象。
     */
    public ArticleTable updateArticle(Long id, ArticleTable articleDetails) {
        if(articleDetails.getVersion() == 0L){
            articleMapper.insertArticle(articleDetails);
        }else{
            articleMapper.updateArticle(id,articleDetails.getVersion()-1,articleDetails);
        }

        ArticleTable articleTable = articleMapper.selectByUniqueIdentifier(articleDetails.getUniqueIdentifier());
        articleTable.setVersion(articleTable.getVersion() + 1);
        return articleTable;
    }

    public ArticleTable getArticle(Long id) {
        var articleTable = articleMapper.selectById(id);
        articleTable.setVersion(articleTable.getVersion() + 1);
        return articleTable;
    }

    public List<ArticleTable> getWrapperArticleByStatusAndKeyWord(Boolean status , String keyWord) {
        return  articleMapper.selectWrapperArticleByStatusAndKeyWord(status,keyWord);
    }

    public Void deleteArticleById(Long id) {
        articleMapper.deleteById(id);
        return null;
    }
}