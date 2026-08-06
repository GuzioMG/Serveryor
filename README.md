# Surwebyor
A web-map frontend for [Surveyor Map Framework](https://modrinth.com/mod/surveyor)

## WARNING: Early alpha alert!
* Currently, there is no support for biome colors for plants/leaves/etc. and grass (water works, though), and every block uses its default map color (green, in case of the aforementioned blocks). This is due to the fact that Minecraft sets them all to `0` (pitch-black) when on the server. First, we'll add support for biome colors in singleplayer/LAN worlds, then we plan to add support for servers via some kind of JSON color export from the client. But neither exists in the mod right now; you gotta be patient.
* The web UI is locked to displaying the overworld (you can cheese it with Inspect Element in the browser, but it's not supported officially)
* There is no support for landmarks/waypoints/players/etc. - only terrain (and, in my honest opinion as the creator itself, even that looks *kinda ugly*, but it's still usable)
* **During the development, my Surveyor data corrupted a couple of times** (the block and biome palettes, to be specific - it would say that leaves were made out of Amethyst, or that I tried to load a biome that doesn't exist). I doubt that it's because of Surwebyor (it only ever reads data from Surveyor), and it's probably from frequent force-stops of the client/server, *but there is a chance that these corruptions are on me* because eg. the mod is accessing too much Surveyor data too quickly. As such, if you decide to use this mod during its alpha period, it's recommended to take frequent backups of your Surveyor data. Actually, frequent backups of *everything* is generally a good practice when playing modded. **Consider yourself warned!**

## Download
* [From Modrinth](https://modrinth.com/mod/surwebyor)
* On GitHub, please build from source or download a [release](https://github.com/GuzioMG/Surwebyor/releases/).
* CurseForge support is not planned.

Additionally, you'll need:
* [Surveyor](https://modrinth.com/mod/surveyor)
* A supported MC version (Fabric 1.22.1 only\* at the moment - higher versions are marked as supported in the JAR in case you want to try going higher, anyway, which **maybe** works because this mod almost doesn't interact with Minecraft APIs (the vast majority are Surveyor's), but it's untested and higher versions aren't marked as supported on Modrinth because of that)
* [Fabric API](https://modrinth.com/mod/fabric-api) and [Fabric Kotlin](https://modrinth.com/mod/fabric-language-kotlin)
* \*Just like with Surveyor itself, NeoForge 1.21.1 via [Sinytra](https://modrinth.com/mod/connector) is considered officially supported (though if you do that, please use [Forgified Fabric API](https://modrinth.com/mod/forgified-fabric-api) instead of Fabric API - Fabric Kotlin can stay, tho, no need to look for a Forge alternative, and they in fact wouldn't work, tho they *can be* present if needed by some other Forge mod). Effectively, we treat Sinytra a multi-loader library, no different from how other mod devs would treat, for example, Architectury or Moonlight Lib.

## Usage
* Get the mod and the dependencies (see above)
* Configure (see below)
* Open http://0.0.0.0:8080 (optionally replace `0.0.0.0` with your server IP/domian if running on a server (and `http://` with `https://`, if said domain is HTTPS-enabled) and `8080` with any other port you chose in the config) in your browser
* Witness *the power of Surveyor, in the tab of your browser!*
That's right! No tile pre-generation that takes 20 years to complete and eats 5TB! It *just works*! All map tiles are generated live, as you request them. Take that, DynMap!

### Help! I uploaded this onto a server, and now all my biomes look the same!
*See: Early alpha alert.*

### Config
Configured via `surwebyor.json` in your `config` folder. The default one is:
```json
{"port":8080,"defaultX":0,"defaultZ":0,"title":"Surwebyor World Map","biomes":[]}
```
...but you're free to format it, to be more readable (we won't overwrite it for you, like some mods do, as long as there are no loading errors).

Fields mean the following
* `port`: The port over which the webmap will be exposed over to the internet. Please note that when you're using this on a server, **you need to forward this port alongside Minecraft's typical one. We'll NOT help you with port-forwarding.** If your hosting doesn't let you forward arbitrary ports, self-host. If your ISP doesn't - find a better ISP.
* `defaultX` and `defaultZ`: Where should the map open by default
* `title`: The title of the website with the map
* `biomes`: *See: Early alpha alert.*

### In-game commands
* `/surwebyor-dump-biomes` - *Not implemented right now, coming soon. See: Early alpha alert.*

## License
* The code and the binaries are licensed under MIT. There are two MIT licenses applied to this project, [one governs most of the code](https://github.com/GuzioMG/Surwebyor?tab=MIT-1-ov-file) and [the other one](https://github.com/GuzioMG/Surwebyor/blob/main/src/main/resources/assets/surwebyor/PNG-library-license.md#license) applies to [Nayuki's PNG library](https://www.nayuki.io/page/png-library) that this project [hard-embeds inside](https://github.com/GuzioMG/Surwebyor/tree/main/src/main/java/io/nayuki/png) (not a soft-dependency via Gradle due to the fact that it doesn't seem like they ever published it onto any Gradle/Maven hosting)
* Yes, that means that you can use this in modpacks. Yes, even in packs outside Modrinth (yep, even CF - just use a direct-file-inclusion), although in that case (to comply with MIT license requirements), you'd need to credit this mod by „including its license in derived work”. Ideally, that'd be a simple link to the license, with the author's and the mod's name, directly in your pack's README's license footnote (like this one) - not buried deep within 2 layers of ZIPs (first the pack, then the mod), even if that's *technically* still inclusion.
* Do not reupload (or **recompile+upload**, unless significant changes were made (and/or this mod got abandoned and you want to keep it updated), as part of a complete fork) this mod as-is onto other hosts, notably 9Minecraft (\*including a virus doesn't count as „significant changes”) or CurseForge. No, CurseForge, I don't care how *technically legal* it would be under MIT. If you refuse to take down any impersonators, then let me call this little string of text „legally binding” (Because it *technically* says License above, so can we really be sure that it's not as valid as the `LICENSE` file on GitHub or the linked license on Modrinth? I mean... Of course it's not (this is merely an explainer footnote), but - you know - can you *really* be sure? How brave would you be to pass over this veil of legal gray-zone, CurseForge?) and see how you roll...
* The icon is literally just a cutout from Surveyor's banner that I superimposed a `w` (instead of v) and a `b` onto, so I'm not even trying to call dibs on it in any way because that's probably not gonna fly. xD
* You cannot directly reuse the Surwebyor name (at least add some prefix, suffix, or something) for any fork or (especially) unrelated projects.

## That's all!
Hope you enjoy this silly little project :3

## Reverse-engineered color data
A private note about PNG's long-color and MC's int-color, and how they map onto RGB colors; it's fine if you have no idea what you're looking at, feel free to disregard it.
```
ALPHA   00000000 00000000 00000000 00000000 00000000 00000000 00000000 11111111   0000000000000000000000000000000000000000000000000000000011111111   255
BLUE    00000000 00000000 00000000 00000000 00000000 11111111 00000000 00000000   0000000000000000000000000000000000000000111111110000000000000000   16711680
GREEN   00000000 00000000 00000000 11111111 00000000 00000000 00000000 00000000   0000000000000000000000001111111100000000000000000000000000000000   1095216660480
RED     00000000 11111111 00000000 00000000 00000000 00000000 00000000 00000000   0000000011111111000000000000000000000000000000000000000000000000   71776119061217280


PLANT 00000000 00000000 01111100 00000000 00000000000000000111110000000000 31744    0,124,0
GREEN 00000000 00000000 11111111 00000000 00000000000000001111111100000000 65280    0,255,0
FIRE  00000000 11111111 00000000 00000000 00000000111111110000000000000000 16711680 255,0,0
RED   00000000 11111111 00000000 00000000 00000000111111110000000000000000 16711680 255,0,0
WATER 00000000 01000000 01000000 11111111 00000000010000000100000011111111 4210943  64,64,255
BLUE  00000000 00000000 00000000 11111111 00000000000000000000000011111111 255      0,0,255
```