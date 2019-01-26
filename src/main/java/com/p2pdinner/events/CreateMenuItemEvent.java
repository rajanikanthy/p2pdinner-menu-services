package com.p2pdinner.events;

import com.p2pdinner.domain.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateMenuItemEvent {
    private final MenuItem menuItem;
}
