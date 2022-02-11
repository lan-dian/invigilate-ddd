package com.hfut.invigilate.business;

import org.w3c.dom.ls.LSInput;

import java.util.List;
import java.util.Set;

public interface InvigilateService {

    void assignWorkIds(List<Integer> workIds, Integer departmentId);

}
