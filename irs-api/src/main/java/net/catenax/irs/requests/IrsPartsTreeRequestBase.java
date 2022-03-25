package net.catenax.irs.requests;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import net.catenax.irs.annotations.ValueOfEnum;
import net.catenax.irs.controllers.ApiErrorsConstants;
import net.catenax.irs.component.enums.Aspect;
import net.catenax.irs.component.enums.Direction;
import net.catenax.irs.component.enums.BomLifecycle;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
abstract public class IrsPartsTreeRequestBase {


   @NotBlank(message = ApiErrorsConstants.INVALID_ARGUMENTS)
   @ValueOfEnum(enumClass = BomLifecycle.class, message = ApiErrorsConstants.ITEM_VIEW_MUST_MATCH_ENUM)
   @Parameter(description = "Unique identifier of a single, unique (sub)component/part/batch,  given by its globalAssetId/ digital twin id",
              in = QUERY, example = "asBuilt", schema = @Schema(implementation = BomLifecycle.class))
   protected final String bomLifecycle;

   @ValueOfEnum(enumClass = Aspect.class, message = ApiErrorsConstants.ITEM_VIEW_MUST_MATCH_ENUM)
   @Parameter(description = "Aspect information to add to the returned tree", in = QUERY, example = "SerialPartTypization", schema = @Schema(implementation = Aspect.class))
   protected final String aspect;

   @Min(value = 1, message = ApiErrorsConstants.ITEM_MIN_DEPTH)
   @Max(value = 100, message = ApiErrorsConstants.ITEM_MAX_DEPTH)
   @Parameter(description = "Max depth of the returned tree, if empty max depth is returned", in = QUERY, schema = @Schema(implementation = Integer.class, minimum = "1", maximum = "100"))
   protected final Integer dept;


   @ValueOfEnum(enumClass = Direction.class, message = ApiErrorsConstants.ITEM_VIEW_MUST_MATCH_ENUM)
   @Parameter(description = "Specifies the direction in which the tree is to be built", in = QUERY, example = "Upward", schema = @Schema(implementation = Aspect.class))
   protected final String direction;

   public String getBomLifecycle() {
      return bomLifecycle;
   }

   public Optional<String> getAspect() {
      return Optional.ofNullable(aspect);
   }

   public Optional<Integer> getDept() {
      return Optional.ofNullable(dept);
   }

   public Optional<String> getDirection() {
      return Optional.ofNullable(direction);
   }
}
