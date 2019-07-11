package com.micerlab.sparrow.eventBus.event.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFileThumbnailEvent {
    String file_id;
    String thumbnail;

    public UpdateFileThumbnailEvent(String file_id, String thumbnail) {
        this.file_id = file_id;
        this.thumbnail = thumbnail;
    }
}
