
package com.ruijing.sequence.xsd;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * MSharpNamespaceHandler
 * <p>
 *
 * @author mwup
 * @version 1.0
 * @created 2018/8/13 11:53
 **/
public class MSharpNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("id-generator", new MSharpIdGeneratorDefinitionParser());
      }
}