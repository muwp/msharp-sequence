package com.ruijing.sequence.namespace.xsd;

import com.ruijing.sequence.sequence.snowflake.SnowflakeSequence;
import com.ruijing.sequence.service.IdGeneratorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * MSharpIdGeneratorDefinitionParser
 *
 * @author mwup
 * @version 1.0
 * @created 2018/8/19 16:41
 **/
public class MSharpIdGeneratorServiceDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class getBeanClass(Element element) {
        return IdGeneratorImpl.class;
    }

    @Override
    protected void doParse(final Element element, final ParserContext parserContext, final BeanDefinitionBuilder bean) {
        //set manager
        final String manager = element.getAttribute("manager");
        if (StringUtils.isBlank(manager)) {
            throw new IllegalArgumentException("manager require no blank");
        }

        bean.addPropertyReference("manager", manager);
        //set dbSequence
        final String dbSequence = element.getAttribute("sequence");
        if (StringUtils.isNotBlank(dbSequence)) {
            bean.addPropertyReference("dbSequence", dbSequence);
        }

        bean.addPropertyValue("snowflakeSequence", new SnowflakeSequence());
    }

    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }
}