module DemoMultipleSprockells where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

prog :: [Instruction]
prog = [
           Branch regSprID (Rel 6)
         , Load (ImmValue 12) reg3
         , WriteInstr reg3 (DirAddr 1) -- Sprockell 1 must jump to second EndProg
         , WriteInstr reg3 (DirAddr 2) -- Sprockell 2 must jump to second EndProg
         , WriteInstr reg3 (DirAddr 3) -- Sprockell 3 must jump to second EndProg
         , Jump (Abs 11)               -- Sprockell 0 jumps to first EndProg
         -- BEGIN: loop
         , ReadInstr (IndAddr regSprID)
         , Receive reg1
         , Compute Equal reg1 reg0 reg2
         , Branch reg2 (Rel (-3))
         -- END: loop
         , Jump (Ind reg1)

         -- 11: Sprockell 0 is sent here
         , EndProg

         -- 12: Sprockells 1, 2 and 3 are sent here
         , EndProg
       ]

demoTest = sysTest [prog,prog,prog,prog]

