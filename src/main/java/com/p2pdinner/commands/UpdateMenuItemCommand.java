package com.p2pdinner.commands;

import com.p2pdinner.domain.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMenuItemCommand {
    @TargetAggregateIdentifier
    private String menuItemId;

    private MenuItem menuItem;
}
