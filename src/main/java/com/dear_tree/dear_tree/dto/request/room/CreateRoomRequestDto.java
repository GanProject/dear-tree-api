package com.dear_tree.dear_tree.dto.request.room;

import com.dear_tree.dear_tree.domain.Icon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoomRequestDto {

    private String roomname;

    private Icon icon;

}
