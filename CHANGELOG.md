# Changelog

## 1.0.6.0

## Feature

- Add more ember blocks
- Add Ceric Heart
- Add Holy Spirit oil Effect
- Add Pan Spell

## Changed

- Empower some effects
- Adjust Food effects
- Eden Apple Tart & Red Vlevet Cake have light model
- Change Foodgeist Gift to Loot Table
- Rework Goodberry spell: 50 mana, 5s cast time , give 10 Goodberry or tranform berry to Goodberry 
- Rework rings: improve eat speed
- Adjust Gospel Knife: add 2.5 damage on undead, remove Smite I
- Adjust TiramisuBook: Add Ubiquitous I

## Fixed

- The repair material of mod's weapon is always iron ingot

## 1.0.5.1

### Fixed

- [Critical] Block Form Food could be duplicated by the sticky piston, see https://github.com/vectorwing/FarmersDelight/issues/1382

## 1.0.5.0

### Feature

- Icebreaker Bread: two-part food block contains two type of food, Iceberg Cream & Iceberg Cream Sandwich
- Goodberry Crate & Icy Egg Crate
- Upgrade Orb Gluttony
- Golden Sparkle particles for Eden Apple Tart
- New Chaos Slash Sound Effect

### Changed

- Improve the model of Red Velvet Cake, Pumpkin Soup, Eden Apple Tart
- Updated Chaos Slash projectile logic
- Make loot chance of wheat book from 1 to 0.3
- Gospel will get Smite 1 when crafting

### Fixed

- the Alchemist pot recipe may give experience twice
- The Gluttony Armors cannot enchanting on the enchanting table
- the Affinity ring lost its texture
- the model of Gluttony Armors has a wrong group, making the belt render wrong
- the models of Ember Bars have z-fighting
- AmethystBeetrootBlock do not break after the supporting block breaking
- localized death messages for Gluttony magic damage lost
- the Foodgeist use the sound of the Zombie
- The Bad Apple Music do not stop after the entity died

### Refactor

- [1.21.1] move Foodgeist Spawn from Player to BlockEntity
- [1.21.1] move curios check from tick to events
- [1.21.1]move from Math.random() to ramdomSource