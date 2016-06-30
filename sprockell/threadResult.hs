module Main where

import BasicFunctions
import HardwareTypes
import Sprockell
import System
import Simulation

program :: [Instruction]
program = [    Load (ImmValue (3)) (reg1),
    Load (ImmValue (3)) (reg2),
    Compute Mul (reg1) (reg2) (reg3),
    WriteInstr (reg3) (DirAddr 9),
    Load (ImmValue (5)) (reg4),
    WriteInstr (reg4) (DirAddr 10),
    ReadInstr (DirAddr 9),
    Receive (reg5),
    Load (ImmValue (9)) (reg6),
    Compute Equal (reg5) (reg6) (reg7),
    Branch (reg7) (Abs (12)),
    Jump (Abs 15),
    ReadInstr (DirAddr 10),
    Receive (reg8),
    WriteInstr (reg8) (DirAddr 9),
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