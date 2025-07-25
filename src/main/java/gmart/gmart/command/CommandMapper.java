package gmart.gmart.command;

import gmart.gmart.dto.item.CreateItemRequestDto;
import gmart.gmart.dto.item.UpdateItemRequestDto;

/**
 * DTO -> COMMAND 로 변경하기 위한 매퍼 클래스
 */
public final class CommandMapper {

    /**
     * @param requestDto CreateItemRequestDto
     * @return CreateItemCommand
     */
    public static CreateItemCommand createItemCommand(CreateItemRequestDto requestDto) {
        return CreateItemCommand.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .itemPrice(requestDto.getItemPrice())
                .deliveryPrice(requestDto.getDeliveryPrice())
                .location(requestDto.getLocation())
                .assemblyStatus(requestDto.getAssemblyStatus())
                .paintStatus(requestDto.getPaintStatus())
                .boxStatus(requestDto.getBoxStatus())
                .dealType(requestDto.getDealType())
                .gundamGrade(requestDto.getGrade())
                .build();
    }

    /**
     * @param requestDto UpdateItemRequestDto
     * @return UpdateItemCommand
     */
    public static UpdateItemCommand updateItemCommand(UpdateItemRequestDto requestDto) {
        return UpdateItemCommand.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .itemPrice(requestDto.getItemPrice())
                .deliveryPrice(requestDto.getDeliveryPrice())
                .location(requestDto.getLocation())
                .assemblyStatus(requestDto.getAssemblyStatus())
                .paintStatus(requestDto.getPaintStatus())
                .boxStatus(requestDto.getBoxStatus())
                .dealType(requestDto.getDealType())
                .gundamGrade(requestDto.getGrade())
                .build();
    }

}
