package com.cmbsj.monitor.model;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.support.index.IndexType;

/**
 * Created by lsy on 2017/7/18.
 */
public class Func extends IdentifiableEntity {
    @Indexed(unique = true)
    private String funcId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "funcName")
    private String funcName;

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public Func(String funcName) {
        this.funcName = funcName;
        this.funcId = "" + funcName.hashCode();
    }

    public Func() {
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Func &&
                ((Func)obj).funcName.equals(this.funcName);
    }

    @Override
    public int hashCode() {
        return funcName.hashCode();
    }
}
