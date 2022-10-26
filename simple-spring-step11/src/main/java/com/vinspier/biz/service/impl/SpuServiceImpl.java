package com.vinspier.biz.service.impl;

import com.vinspier.biz.service.SpuService;
import com.vinspier.springframework.context.annotation.Scope;
import com.vinspier.springframework.stereotype.Component;

@Component("spuService")
@Scope(value = "singleton")
public class SpuServiceImpl implements SpuService {
}
