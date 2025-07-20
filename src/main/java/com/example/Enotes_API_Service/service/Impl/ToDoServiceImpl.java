package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.ToDoDto;
import com.example.Enotes_API_Service.entity.ToDo;
import com.example.Enotes_API_Service.enums.ToDoStatus;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.repository.ToDoRepository;
import com.example.Enotes_API_Service.service.ToDoService;
import com.example.Enotes_API_Service.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveToDo(ToDoDto toDoDto) throws Exception {
        // Validation
        validation.toDoValidation(toDoDto);

        ToDo toDo = mapper.map(toDoDto, ToDo.class);
        toDo.setStatusId(toDoDto.getStatus().getId());
        ToDo saveToDo = toDoRepository.save(toDo);
        return !ObjectUtils.isEmpty(saveToDo);
    }

    @Override
    public ToDoDto getToDoById(Integer id) throws Exception{
        ToDo toDo = toDoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ToDo not found! Invalid id."));
        ToDoDto toDoDto = mapper.map(toDo, ToDoDto.class);
        setStatus(toDoDto, toDo);
        return toDoDto;
    }

    private void setStatus(ToDoDto toDoDto, ToDo toDo) {
        for(ToDoStatus st: ToDoStatus.values()){
            if(st.getId().equals(toDo.getStatusId())){
                ToDoDto.StatusDto statusDto = ToDoDto.StatusDto.builder()
                        .id(st.getId())
                        .name(st.getName())
                        .build();
                toDoDto.setStatus(statusDto);
            }
        }
    }

    @Override
    public List<ToDoDto> getToDoByUser() {
        Integer userId = 1;
        List<ToDo> toDos = toDoRepository.findByCreatedBy(userId);
        return toDos.stream().map(td -> mapper.map(td, ToDoDto.class)).toList();
    }
}
