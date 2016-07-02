module BasicFunctions where

-- ==========================================================================================================
-- Some elementary constants and Functions
-- ==========================================================================================================
reg0          = 0    :: Int                                 -- names for registers. reg0 is ALWAYS 0
regSprID      = 1    :: Int                                 -- regSprID: contains the sprockellID
reg1          = 2    :: Int                                 -- registers 1-21 for other usage
reg2          = 3    :: Int
reg3          = 4    :: Int
reg4          = 5    :: Int
reg5          = 6    :: Int
reg6          = 7    :: Int
reg7          = 8    :: Int
reg8          = 9    :: Int
reg9          = 10   :: Int
reg10         = 11   :: Int
reg11         = 12   :: Int
reg12         = 13   :: Int
reg13         = 14   :: Int
reg14         = 15   :: Int
reg15         = 16   :: Int
reg16         = 17   :: Int
reg17         = 18   :: Int
reg18         = 19   :: Int
reg19         = 20   :: Int
reg20         = 21   :: Int
reg21         = 22   :: Int

intBool True  = 1                                               -- Bool-to-Int
intBool False = 0

x +>> xs = [x] ++ init xs                                       -- shift value into buffer
xs <<+ x = tail xs ++ [x]

f  $>  xs = map f xs
fs |$| xs = zipWith (\f x -> f x) fs xs                         -- parallel application of a list of functions
                                                                -- to an equally long list of arguments

(!)  :: [a] -> Int -> a                                         -- list indexing
xs ! i = xs !! i

(<~) :: [a] -> (Int, a) -> [a]                                  -- put value x at address i in xs
xs <~ (i,x) = take i xs ++ [x] ++ drop (i+1) xs

(<~!) :: [a] -> (Int, a) -> [a]                                 -- ibid, but leave address 0 unchanged
xs <~! (i,x)    | i == 0        = xs
                | otherwise     = xs <~ (i,x)

x ∈ xs =  x `elem` xs

concatWith x [] = []                                            -- concats a list of lists with a "gluing element" x
concatWith x [y] = y
concatWith x (y:ys) = y ++ [x] ++ concatWith x ys

ljustify n x = x ++ replicate (n - length x) ' '                -- adds spaces upto n positions for outlining;
rjustify n x = replicate (n - length x) ' ' ++ x                -- may be used for your own show-function


