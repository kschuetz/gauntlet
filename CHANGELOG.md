# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## [Unreleased]

No changes yet

## [0.5.0] - 2021-01-21

### Changed

- Root package is now "software.kes.gauntlet"

## [0.4.4] - 2021-01-21

### Added

- Add shrink strategies for strings and characters

### Changed

- Improve performance of `ShrinkCollection`

## [0.4.3] - 2020-10-02

### Added

- Add built-in arbitraries:
  - `alphaStrings`
  - `alphaUpperStrings`
  - `alphaLowerStrings`
  - `alphanumericStrings`
  - `identifiers`
  - `alphaCharacters`
    - `alphaUpperCharacters`
  - `alphaLowerCharacters`
  - `alphanumericCharacters`
  - `numericCharacters`
  - `punctuationCharacters`
  - `asciiPrintableCharacters`
  - `controlCharacters`

### Changed

- Upgrade Kraftwerk to 0.10.0

[Unreleased]: https://github.com/kschuetz/gauntlet/compare/gauntlet-0.5.0...HEAD

[0.5.0]: https://github.com/kschuetz/gauntlet/compare/gauntlet-0.4.4...gauntlet-0.5.0

[0.4.4]: https://github.com/kschuetz/gauntlet/compare/gauntlet-0.4.3...gauntlet-0.4.4

[0.4.3]: https://github.com/kschuetz/gauntlet/commits/gauntlet-0.4.3
