module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (3)) (reg1),
    Store (reg1) (DirAddr 1),
    Load (DirAddr 1) (reg3),
    ComputeI Add (ImmValue (4)) (reg3) (reg4),
    Store (reg4) (DirAddr 1)
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