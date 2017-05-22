Pawn Chess
==========

For the implementation I tried various approaches in order to achieve a workable solution.
A first attempt of using Minimax did not work properly in time, due to the time limit, which 
allowed only to look ahead a few single steps. Additionally, the branching factor of 3 for 16 
possible fields didn't help either... (although, I could've removed some of the available positions 
if they were blocked, but still would end up in the beginning with a branching factor of 3, now for only 8 fields...)

So, I dropped that approach, and though about CSPs, but this approach does not seem to be applicable
for such a problem, since making constraints on a single field depending on the current
state of the game is not so straight forward... Additionally, also here, backtracking might use
much time...

Eventually, I decided to go for some more or less sophisticated rules and the combined application
of them, evaluating the game's current state. Using this, maybe a win-rate of more than 50% 
could be achieved in the longer run, especially, if playing as the first participant...

So, there we go: Rules are located in the package `matileraphael.rulebase.rules` whereas the `RuleBasedAgent`
allows to be configured with them. The `matileraphael.commons` package only defines some classes containing
the current game state, and yet another implementation of the usually quite helpful but missing `Pair<K,V>`... 