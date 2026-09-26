package com.sakalti.vinegar.api.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.food.FoodProperties;

import java.util.function.Supplier;

/**
 * Vinegar Food API for NeoForge 26.3.
 *
 * Provides a convenient builder for food items with:
 * - Nutrition
 * - Saturation
 * - Always edible
 * - Fast consumption
 * - Multiple status effects
 * - Per-effect probability
 * - Custom consumption time
 *
 * The implementation uses Minecraft's Data Components system.
 */
public final class FoodApi {

    private FoodApi() {
    }

    /**
     * Initializes the Food API.
     *
     * Reserved for future API initialization.
     */
    public static void init() {
    }

    /**
     * Creates a new food builder.
     *
     * @param nutrition hunger points restored
     * @param saturation saturation modifier
     */
    public static Builder create(int nutrition, float saturation) {
        return new Builder(nutrition, saturation);
    }

    /**
     * Convenient food builder.
     */
    public static final class Builder {

        private final FoodProperties.Builder foodBuilder;

        private float consumeSeconds = 1.6F;

        private boolean alwaysEdible = false;

        private boolean fast = false;

        public Builder(int nutrition, float saturation) {
            if (nutrition < 0) {
                throw new IllegalArgumentException(
                        "Food nutrition cannot be negative"
                );
            }

            if (saturation < 0.0F) {
                throw new IllegalArgumentException(
                        "Food saturation cannot be negative"
                );
            }

            this.foodBuilder = new FoodProperties.Builder()
                    .nutrition(nutrition)
                    .saturationModifier(saturation);
        }

        /**
         * Allows the food to be eaten while the hunger bar is full.
         */
        public Builder alwaysEdible() {
            this.alwaysEdible = true;
            return this;
        }

        /**
         * Makes the food consume faster.
         */
        public Builder fast() {
            this.fast = true;
            return this;
        }

        /**
         * Sets the consumption time in seconds.
         *
         * @param seconds consumption time
         */
        public Builder consumeSeconds(float seconds) {
            if (seconds <= 0.0F) {
                throw new IllegalArgumentException(
                        "Consumption time must be greater than zero"
                );
            }

            this.consumeSeconds = seconds;
            return this;
        }

        /**
         * Adds a status effect.
         *
         * @param effect effect supplier
         * @param duration duration in ticks
         * @param amplifier effect amplifier
         */
        public Builder effect(
                Supplier<? extends MobEffect> effect,
                int duration,
                int amplifier
        ) {
            return effect(effect, duration, amplifier, 1.0F);
        }

        /**
         * Adds a status effect with a probability.
         *
         * @param effect effect supplier
         * @param duration duration in ticks
         * @param amplifier effect amplifier
         * @param probability probability from 0.0 to 1.0
         */
        public Builder effect(
                Supplier<? extends MobEffect> effect,
                int duration,
                int amplifier,
                float probability
        ) {
            if (duration <= 0) {
                throw new IllegalArgumentException(
                        "Effect duration must be greater than zero"
                );
            }

            if (amplifier < 0) {
                throw new IllegalArgumentException(
                        "Effect amplifier cannot be negative"
                );
            }

            if (probability < 0.0F || probability > 1.0F) {
                throw new IllegalArgumentException(
                        "Effect probability must be between 0.0 and 1.0"
                );
            }

            foodBuilder.effect(
                    () -> new MobEffectInstance(
                            effect.get(),
                            duration,
                            amplifier
                    ),
                    probability
            );

            return this;
        }

        /**
         * Adds a status effect using a preconfigured instance.
         */
        public Builder effect(
                Supplier<MobEffectInstance> effect,
                float probability
        ) {
            if (probability < 0.0F || probability > 1.0F) {
                throw new IllegalArgumentException(
                        "Effect probability must be between 0.0 and 1.0"
                );
            }

            foodBuilder.effect(effect, probability);

            return this;
        }

        /**
         * Builds the FoodProperties.
         */
        public FoodProperties buildFood() {
            if (alwaysEdible) {
                foodBuilder.alwaysEdible();
            }

            if (fast) {
                foodBuilder.fast();
            }

            return foodBuilder.build();
        }

        /**
         * Builds Item.Properties containing the food
         * and consumption components.
         */
        public Item.Properties build() {
            FoodProperties food = buildFood();

            return new Item.Properties()
                    .food(food)
                    .component(
                            net.minecraft.core.component.DataComponents.CONSUMABLE,
                            Consumable.builder()
                                    .consumeSeconds(consumeSeconds)
                                    .build()
                    );
        }
    }
}
