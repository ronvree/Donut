module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (3)) (reg1),
    Store (reg1) (DirAddr 1),
    Load (ImmValue (0)) (reg2),
    BranchF (reg2) (Abs (7)),
    Load (ImmValue (5)) (reg3),
    Store (reg3) (DirAddr 1),
    Nop,
    ReadInstr (IndAddr (reg0)),
    Receive (reg0),
    EndProg
    ]

thread1 :: [Instruction]
thread1 = [    EndProg
    ]

thread2 :: [Instruction]
thread2 = [    EndProg
    ]

thread3 :: [Instruction]
thread3 = [    EndProg
    ]


main = sysTest [program,thread1,thread2,thread3]