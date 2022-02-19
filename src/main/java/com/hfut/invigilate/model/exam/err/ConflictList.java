package com.hfut.invigilate.model.exam.err;

import java.util.ArrayList;

public class ConflictList extends ArrayList<ExamConflict> {

    private int successCount;

    public boolean add(String name,Integer line,String reason) {
        return add(new ExamConflict(name,line,reason));
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
}
