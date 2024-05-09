package org.ferhat.vetmanagement.core.config.modelMapper;

import org.modelmapper.ModelMapper;

public interface IModelMapperService {

    ModelMapper forRequest();

    ModelMapper forResponse();
}
